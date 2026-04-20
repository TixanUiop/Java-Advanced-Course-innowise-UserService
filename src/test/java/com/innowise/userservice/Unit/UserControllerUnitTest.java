package com.innowise.userservice.Unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.innowise.userservice.controller.UserController;
import com.innowise.userservice.dto.CreateUserDTO;
import com.innowise.userservice.dto.UserDTO;
import com.innowise.userservice.exception.UserAlreadyExistsException;
import com.innowise.userservice.exception.UserNotFoundException;
import com.innowise.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerUnitTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private UserDTO userDTO;
    private CreateUserDTO createUserDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        createUserDTO = new CreateUserDTO();
        createUserDTO.setName("Alice");
        createUserDTO.setSurname("Smith");
        createUserDTO.setEmail("alice@test.com");
        createUserDTO.setBirthDate(LocalDate.of(1990, 1, 1));

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("Alice");
        userDTO.setSurname("Smith");
        userDTO.setEmail("alice@test.com");
        userDTO.setBirthDate(LocalDate.of(1990, 1, 1));
        userDTO.setActive(true);
    }

    @Test
    void testCreateUserSuccess() throws Exception {
        when(userService.create(any(CreateUserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/api/v1/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@test.com"));
    }

    @Test
    void testCreateUserValidationFail() throws Exception {
        CreateUserDTO invalidDto = new CreateUserDTO();
        invalidDto.setName(null); // нарушение @NotNull

        mockMvc.perform(post("/api/v1/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetUserByIdSuccess() throws Exception {
        when(userService.getById(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }


    @Test
    void testUpdateUserSuccess() throws Exception {
        when(userService.update(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(put("/api/v1/users/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void testDeleteUser() throws Exception {
        when(userService.delete(1L)).thenReturn(userDTO);

        mockMvc.perform(delete("/api/v1/users/delete/1"))
                .andExpect(status().isNoContent());
    }


}