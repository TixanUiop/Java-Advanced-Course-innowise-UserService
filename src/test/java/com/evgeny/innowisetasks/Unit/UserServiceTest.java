package com.evgeny.innowisetasks.Unit;

import com.evgeny.innowisetasks.DTO.CreateUserDTO;
import com.evgeny.innowisetasks.DTO.PaymentCardsDTO;
import com.evgeny.innowisetasks.DTO.UserDTO;
import com.evgeny.innowisetasks.Entity.PaymentCardsEntity;
import com.evgeny.innowisetasks.Entity.UserEntity;
import com.evgeny.innowisetasks.Exception.StatusChangeErrorException;
import com.evgeny.innowisetasks.Exception.UserAlreadyExistsException;
import com.evgeny.innowisetasks.Exception.UserNotFoundException;
import com.evgeny.innowisetasks.Mapper.UserMapper;
import com.evgeny.innowisetasks.Repository.UserRepository;
import com.evgeny.innowisetasks.Service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {

        user = new UserEntity();
        user.setId(1L);
        user.setName("test");
        user.setSurname("test");
        user.setEmail("test@mail.com");
        user.setActive(true);

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("test");
        userDTO.setSurname("test");
        userDTO.setEmail("test@mail.com");
        userDTO.setActive(true);
    }

    @Test
    void createShouldCreateUser() {

        CreateUserDTO createDto = new CreateUserDTO("test", "test", LocalDate.now(), "testemail@yandex.com", true);

        when(userRepository.existsByEmail(createDto.getEmail())).thenReturn(false);
        when(userMapper.toEntity(createDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.create(createDto);

        assertNotNull(result);
        assertEquals(userDTO.getEmail(), result.getEmail());

        verify(userRepository).save(user);
    }

    @Test
    void createShouldThrowExceptionIfEmailExists() {

        CreateUserDTO createDto = new CreateUserDTO("test", "test", LocalDate.now(), "testemailyandex.com", true);

        when(userRepository.existsByEmail(createDto.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> userService.create(createDto));
    }

    @Test
    void getByIdShouldReturnUser() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getById(1L);

        assertEquals("test", result.getName());
    }

    @Test
    void getByIdShouldThrowException() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.getById(1L));
    }

    @Test
    void deleteShouldDeleteUser() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.delete(1L);

        verify(userRepository).delete(user);
        assertEquals(userDTO.getId(), result.getId());
    }

    @Test
    void updateShouldUpdateUser() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.update(1L, userDTO);

        assertEquals("test", result.getName());

        verify(userRepository).findById(1L);
    }

    @Test
    void activateShouldActivateUser() {

        when(userRepository.updateActiveStatusJPQL(1L,true)).thenReturn(1);

        Boolean result = userService.activate(1L);

        assertTrue(result);
    }

    @Test
    void activateShouldThrowException() {

        when(userRepository.updateActiveStatusJPQL(1L,true)).thenReturn(0);

        assertThrows(StatusChangeErrorException.class,
                () -> userService.activate(1L));
    }

    @Test
    void deactivateShouldDeactivateUser() {

        when(userRepository.updateActiveStatusJPQL(1L,false)).thenReturn(1);

        Boolean result = userService.deactivate(1L);

        assertTrue(result);
    }

    @Test
    void getAllShouldReturnPage() {

        Pageable pageable = PageRequest.of(0,10);

        Page<UserEntity> page =
                new PageImpl<>(List.of(user));

        when(userRepository.findByNameAndSurnameNative("test","test",pageable))
                .thenReturn(page);

        when(userMapper.toDTO(user)).thenReturn(userDTO);

        Page<UserDTO> result = userService.getAll("test","test",pageable);

        assertEquals(1,result.getTotalElements());
    }

    @Test
    void getCardsByUserIdShouldReturnCards() {

        PaymentCardsEntity card = new PaymentCardsEntity();
        card.setId(1L);
        card.setNumber("1234");
        card.setHolder("test");
        card.setExpirationDate(LocalDate.now());
        card.setActive(true);

        user.setCards(List.of(card));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        List<PaymentCardsDTO> result = userService.getCardsByUserId(1L);

        assertEquals(1,result.size());
        assertEquals("1234",result.get(0).getNumber());
    }
}
