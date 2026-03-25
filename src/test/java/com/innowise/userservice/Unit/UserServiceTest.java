package com.innowise.userservice.Unit;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import com.innowise.userservice.dto.CreateUserDTO;
import com.innowise.userservice.dto.PaymentCardsDTO;
import com.innowise.userservice.dto.UserDTO;
import com.innowise.userservice.entity.PaymentCardsEntity;
import com.innowise.userservice.entity.UserEntity;
import com.innowise.userservice.exception.StatusChangeErrorException;
import com.innowise.userservice.exception.UserAlreadyExistsException;
import com.innowise.userservice.exception.UserNotFoundException;
import com.innowise.userservice.mapper.UserMapper;
import com.innowise.userservice.repository.UserRepository;
import com.innowise.userservice.service.UserServiceImpl;
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
import org.springframework.data.jpa.domain.Specification;

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
    void deleteShouldDeactivateUser() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.delete(1L);

        assertFalse(user.getActive());
        if (user.getCards() != null) {
            user.getCards().forEach(card -> assertFalse(card.getActive()));
        }
        assertEquals(userDTO.getId(), result.getId());

        verify(userRepository).save(user);
    }

    @Test
    void updateShouldUpdateUser() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);
        when(userRepository.save(user)).thenReturn(user);

        UserDTO result = userService.update(userDTO);

        assertEquals("test", result.getName());

        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
        verify(userMapper).toDTO(user);
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

        when(userRepository.findAll(any(Specification.class), eq(pageable)))
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

    @Test
    void getCardsByUserIdShouldReturnEmptyListIfNoCards() {
        user.setCards(List.of());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        List<PaymentCardsDTO> result = userService.getCardsByUserId(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteShouldWorkWhenUserHasNoCards() {
        user.setCards(null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.delete(1L);

        assertFalse(user.getActive());
        assertEquals(userDTO.getId(), result.getId());

        verify(userRepository).save(user);
    }

    @Test
    void updateShouldThrowExceptionIfUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.update(userDTO));
    }

    @Test
    void getAllShouldReturnEmptyPageIfNoUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserEntity> emptyPage = new PageImpl<>(List.of());
        when(userRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(emptyPage);

        Page<UserDTO> result = userService.getAll("unknown", "unknown", pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }
}
