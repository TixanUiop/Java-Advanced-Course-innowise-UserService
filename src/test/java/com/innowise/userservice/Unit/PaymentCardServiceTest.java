package com.innowise.userservice.Unit;

import com.innowise.userservice.dto.CreatePaymentCardsDTO;
import com.innowise.userservice.dto.PaymentCardsDTO;
import com.innowise.userservice.entity.PaymentCardsEntity;
import com.innowise.userservice.entity.UserEntity;
import com.innowise.userservice.exception.CardLimitExceededException;
import com.innowise.userservice.exception.CardNotFoundException;
import com.innowise.userservice.exception.CardStatusChangeException;
import com.innowise.userservice.exception.UserNotFoundException;
import com.innowise.userservice.mapper.PaymentCardsMapper;
import com.innowise.userservice.repository.PaymentCardsRepository;
import com.innowise.userservice.repository.UserRepository;
import com.innowise.userservice.service.PaymentCardServiceImpl;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentCardServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentCardsRepository cardRepository;

    @Mock
    private PaymentCardsMapper mapper;

    @InjectMocks
    private PaymentCardServiceImpl paymentCardService;

    private PaymentCardsEntity card;
    private PaymentCardsDTO cardDTO;
    private UserEntity user;

    @BeforeEach
    void setUp() {

        card = PaymentCardsEntity.builder()
                .id(1L)
                .number("1234")
                .active(true)
                .expirationDate(LocalDate.of(2030,1,1))
                .holder("Test User")
                .build();

        cardDTO = PaymentCardsDTO.builder()
                .id(1L)
                .number("1234")
                .active(true)
                .expirationDate(LocalDate.of(2030,1,1))
                .holder("Test User")
                .build();

        user = UserEntity.builder()
                .id(1L)
                .name("Test")
                .surname("User")
                .email("test@mail.com")
                .cards(new ArrayList<>())
                .build();
    }

    @Test
    void shouldCreateSuccessfully() {

        CreatePaymentCardsDTO createPaymentCardsDTO =
                new CreatePaymentCardsDTO("1234", LocalDate.now(), true, 7L);

        user.setCards(new ArrayList<>());

        when(userRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(user));
        when(mapper.createToEntity(createPaymentCardsDTO)).thenReturn(card);
        when(mapper.toDTO(card)).thenReturn(cardDTO);

        PaymentCardsDTO result = paymentCardService.create(createPaymentCardsDTO);

        assertNotNull(result);
        assertEquals(cardDTO.getNumber(), result.getNumber());

        verify(userRepository).findByIdForUpdate(7L);
        verify(mapper).createToEntity(createPaymentCardsDTO);
        verify(mapper).toDTO(card);

    }

    @Test
    void createShouldThrowUserNotFound() {

        CreatePaymentCardsDTO dto = new CreatePaymentCardsDTO("1234", LocalDate.now(), true, 1L);

        when(userRepository.findByIdForUpdate(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> paymentCardService.create(dto));

        verify(userRepository).findByIdForUpdate(1L);
    }

    @Test
    void createShouldThrowCardLimitExceeded() {

        long userId = 7L;

        CreatePaymentCardsDTO dto = new CreatePaymentCardsDTO(
                "1234",
                LocalDate.now(),
                true,
                userId
        );

        user.setId(userId);
        user.setCards(new ArrayList<>(Arrays.asList(card, card, card, card, card)));

        when(userRepository.findByIdForUpdate(userId)).thenReturn(Optional.of(user));
        when(cardRepository.countByUserId(userId)).thenReturn(5L);

        assertThrows(CardLimitExceededException.class,
                () -> paymentCardService.create(dto));
    }

    @Test
    void getByIdShouldReturnCard() {

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(mapper.toDTO(card)).thenReturn(cardDTO);

        PaymentCardsDTO result = paymentCardService.getById(1L);

        assertNotNull(result);
        assertEquals(cardDTO.getNumber(), result.getNumber());

        verify(cardRepository).findById(1L);
    }

    @Test
    void getByIdShouldThrowException() {

        when(cardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class,
                () -> paymentCardService.getById(1L));
    }

    @Test
    void getAllShouldReturnPage() {

        Pageable pageable = PageRequest.of(0,10);

        Page<PaymentCardsEntity> page =
                new PageImpl<>(List.of(card));

        when(cardRepository.findAll(pageable)).thenReturn(page);
        when(mapper.toDTO(card)).thenReturn(cardDTO);

        Page<PaymentCardsDTO> result =
                paymentCardService.getAll(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getAllByUserIdShouldReturnCards() {

        when(cardRepository.findAllByUserId(1L))
                .thenReturn(List.of(card));

        when(mapper.toDTO(card)).thenReturn(cardDTO);

        List<PaymentCardsDTO> result =
                paymentCardService.getAllByUserId(1L);

        assertEquals(1, result.size());
    }

    @Test
    void updateShouldUpdateCard() {

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(mapper.toDTO(card)).thenReturn(cardDTO);

        PaymentCardsDTO result =
                paymentCardService.update(cardDTO);

        assertNotNull(result);
        verify(cardRepository).findById(1L);
    }

    @Test
    void updateShouldThrowCardNotFound() {

        when(cardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class,
                () -> paymentCardService.update(cardDTO));
    }

    @Test
    void activateShouldReturnTrue() {

        when(cardRepository.updateActiveStatusPayJPQL(1L,true))
                .thenReturn(1);

        Boolean result = paymentCardService.activate(1L);

        assertTrue(result);
    }

    @Test
    void activateShouldThrowException() {

        when(cardRepository.updateActiveStatusPayJPQL(1L,true))
                .thenReturn(0);

        assertThrows(CardStatusChangeException.class,
                () -> paymentCardService.activate(1L));
    }

    @Test
    void deactivateShouldReturnTrue() {

        when(cardRepository.updateActiveStatusPayJPQL(1L,false))
                .thenReturn(1);

        Boolean result = paymentCardService.deactivate(1L);

        assertTrue(result);
    }

    @Test
    void deactivateShouldThrowException() {

        when(cardRepository.updateActiveStatusPayJPQL(1L,false))
                .thenReturn(0);

        assertThrows(CardStatusChangeException.class,
                () -> paymentCardService.deactivate(1L));
    }

    @Test
    void deleteShouldDeleteCard() {

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(mapper.toDTO(card)).thenReturn(cardDTO);

        PaymentCardsDTO result =
                paymentCardService.delete(1L);

        assertNotNull(result);

        verify(cardRepository).delete(card);
    }

    @Test
    void deleteShouldThrowException() {

        when(cardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class,
                () -> paymentCardService.delete(1L));
    }

    @Test
    void updateShouldPreserveUserRelation() {

        card.setUser(user);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(mapper.toDTO(card)).thenReturn(cardDTO);

        PaymentCardsDTO result = paymentCardService.update(cardDTO);

        assertNotNull(result);
        assertEquals(user, card.getUser());
    }


}
