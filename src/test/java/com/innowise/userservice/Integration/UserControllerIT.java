package com.innowise.userservice.Integration;

import com.innowise.userservice.Util.TestJwtUtil;
import com.innowise.userservice.dto.CreateUserDTO;
import com.innowise.userservice.dto.UserDTO;
import com.innowise.userservice.entity.UserEntity;
import com.innowise.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
public class UserControllerIT {

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

    private UserEntity user;
    private String adminToken;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        user = new UserEntity();
        user.setName("John");
        user.setSurname("Doe");
        user.setEmail("john@test.com");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setActive(true);
        user.setCards(new ArrayList<>());
        user = userRepository.save(user);

        adminToken = TestJwtUtil.generateTestToken(1L, "ADMIN");
    }

    private HttpHeaders headersWithToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private UserDTO createUserViaApi(String name, String surname, String email) {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setName(name);
        dto.setSurname(surname);
        dto.setEmail(email);
        dto.setBirthDate(LocalDate.of(1990, 1, 1));

        HttpEntity<CreateUserDTO> request = new HttpEntity<>(dto, headersWithToken(adminToken));
        ResponseEntity<UserDTO> response =
                restTemplate.exchange("/api/v1/users/create", HttpMethod.POST, request, UserDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return response.getBody();
    }

    @Test
    void testCreateUser() {
        UserDTO user = createUserViaApi("Alice", "Smith", "alice@test.com");
        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo("Alice");
        assertThat(user.getEmail()).isEqualTo("alice@test.com");
    }

    @Test
    void testGetUserById() {
        HttpEntity<Void> request = new HttpEntity<>(headersWithToken(adminToken));
        ResponseEntity<UserDTO> response =
                restTemplate.exchange("/api/v1/users/" + user.getId(),
                        HttpMethod.GET, request, UserDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(user.getId());
    }

    @Test
    void testUpdateUser() {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName("JohnUpdated");
        dto.setSurname("DoeUpdated");
        dto.setEmail("johnupdated@test.com");
        dto.setBirthDate(LocalDate.of(1990,1,1));
        dto.setActive(true);

        HttpEntity<UserDTO> request = new HttpEntity<>(dto, headersWithToken(adminToken));

        ResponseEntity<UserDTO> response =
                restTemplate.exchange("/api/v1/users/update/" + dto.getId(),
                        HttpMethod.PUT, request, UserDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("JohnUpdated");
    }

    @Test
    void testActivateDeactivateUser() {
        HttpEntity<Void> request = new HttpEntity<>(headersWithToken(adminToken));

        ResponseEntity<Void> deactivate =
                restTemplate.exchange("/api/v1/users/deactivate/" + user.getId(),
                        HttpMethod.POST, request, Void.class);
        assertThat(deactivate.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<Void> activate =
                restTemplate.exchange("/api/v1/users/activate/" + user.getId(),
                        HttpMethod.POST, request, Void.class);
        assertThat(activate.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void testDeleteUser() {
        HttpEntity<Void> request = new HttpEntity<>(headersWithToken(adminToken));
        restTemplate.exchange("/api/v1/users/delete/" + user.getId(),
                HttpMethod.DELETE, request, Void.class);

        UserEntity deletedUser = userRepository.findById(user.getId()).orElseThrow();

        assertFalse(deletedUser.getActive());
        if (deletedUser.getCards() != null) {
            deletedUser.getCards().forEach(card -> assertFalse(card.getActive()));
        }
    }

    @Test
    void testGetAllUsers() {
        createUserViaApi("Alice", "Smith", "alice@test.com");
        createUserViaApi("Bob", "Brown", "bob@test.com");

        HttpEntity<Void> request = new HttpEntity<>(headersWithToken(adminToken));

        ResponseEntity<Map<String, Object>> response =
                restTemplate.exchange("/api/v1/users?page=0&size=10",
                        HttpMethod.GET, request, new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();

        List<?> users = (List<?>) body.get("content");
        assertThat(users.size()).isGreaterThanOrEqualTo(3);
    }

    @Test
    void testGetCardsByUserId() {
        HttpEntity<Void> request = new HttpEntity<>(headersWithToken(adminToken));

        ResponseEntity<List> response =
                restTemplate.exchange("/api/v1/users/get-cards-users-by-id/" + user.getId(),
                        HttpMethod.GET, request, List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}