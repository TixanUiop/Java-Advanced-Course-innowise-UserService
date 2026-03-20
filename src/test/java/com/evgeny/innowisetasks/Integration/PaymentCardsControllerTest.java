package com.evgeny.innowisetasks.Integration;

import com.evgeny.innowisetasks.DTO.CreatePaymentCardsDTO;
import com.evgeny.innowisetasks.DTO.PaymentCardsDTO;
import com.evgeny.innowisetasks.Entity.PaymentCardsEntity;
import com.evgeny.innowisetasks.Entity.UserEntity;
import com.evgeny.innowisetasks.Repository.PaymentCardsRepository;
import com.evgeny.innowisetasks.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
public class PaymentCardsControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    static {
        postgres.start();
        System.setProperty("spring.datasource.url", postgres.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgres.getUsername());
        System.setProperty("spring.datasource.password", postgres.getPassword());
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentCardsRepository cardsRepository;

    private UserEntity user;

    @BeforeEach
    void setup() {

        cardsRepository.deleteAll();
        userRepository.deleteAll();

        user = new UserEntity();
        user.setId(1L);
        user.setName("John");
        user.setSurname("Doe");
        user.setEmail("john@test.com");
        user.setBirthDate(LocalDate.of(1990,1,1));
        user.setActive(true);

        user.setCards(new ArrayList<>());

        user = userRepository.save(user);
    }

    @Test
    void testGetAllPaymentCards() {

        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/v1/payment-cards?page=0&size=10", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("content");
    }

    @Test
    void testCreateCard() {

        CreatePaymentCardsDTO dto = new CreatePaymentCardsDTO();
        dto.setNumber("1234567812345678");
        dto.setExpirationDate(LocalDate.of(2030,12,31));
        dto.setActive(true);
        dto.setUserId(user.getId());

        ResponseEntity<PaymentCardsDTO> response =
                restTemplate.postForEntity("/api/v1/payment-cards/create", dto, PaymentCardsDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getNumber()).isEqualTo("1234567812345678");
    }

    @Test
    void testGetCardById() {

        PaymentCardsEntity cardEntity = createCardInDb();

        ResponseEntity<PaymentCardsDTO> response =
                restTemplate.getForEntity("/api/v1/payment-cards/" + cardEntity.getId(), PaymentCardsDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(cardEntity.getId());
    }

    @Test
    void testUpdateCard() {

        PaymentCardsEntity cardEntity = createCardInDb();

        cardEntity.setActive(false);

        HttpEntity<PaymentCardsEntity> request = new HttpEntity<>(cardEntity);

        ResponseEntity<PaymentCardsDTO> response =
                restTemplate.exchange(
                        "/api/v1/payment-cards/update",
                        HttpMethod.PUT,
                        request,
                        PaymentCardsDTO.class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getActive()).isFalse();
    }

    @Test
    void testActivateDeactivate() {

        PaymentCardsEntity cardEntity = createCardInDb();

        ResponseEntity<Void> deactivate =
                restTemplate.postForEntity("/api/v1/payment-cards/deactivate/" + cardEntity.getId(), null, Void.class);

        assertThat(deactivate.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<Void> activate =
                restTemplate.postForEntity("/api/v1/payment-cards/activate/" + cardEntity.getId(), null, Void.class);

        assertThat(activate.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void testDeleteCard() {
        PaymentCardsEntity cardEntity = createCardInDb();
        restTemplate.delete("/api/v1/payment-cards/delete/" + cardEntity.getId());
        Optional<PaymentCardsEntity> deleted = cardsRepository.findById(cardEntity.getId());
        assertThat(deleted).isNotPresent();
        ResponseEntity<String> response =
        restTemplate.getForEntity("/api/v1/payment-cards/" + cardEntity.getId(), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    private PaymentCardsEntity createCardInDb() {

        PaymentCardsEntity card = new PaymentCardsEntity();

        card.setNumber("1234567812345678");
        card.setHolder("John Doe");
        card.setExpirationDate(LocalDate.of(2030,12,31));
        card.setActive(true);
        card.setUser(user);

        return cardsRepository.save(card);
    }
}