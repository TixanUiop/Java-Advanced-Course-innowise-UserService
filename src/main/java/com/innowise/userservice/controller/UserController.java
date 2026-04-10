package com.innowise.userservice.controller;


import com.innowise.userservice.dto.CreateUserDTO;
import com.innowise.userservice.dto.PaymentCardsDTO;
import com.innowise.userservice.dto.UserDTO;
import com.innowise.userservice.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PreAuthorize("hasRole('ADMIN') or authentication.principal == #id")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@Email @NotBlank @PathVariable String email) {
        UserDTO user = userService.getByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN') or authentication.principal == #id")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@Positive @PathVariable Long id) {
        UserDTO user = userService.getById(id);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<UserDTO> create(@Valid @RequestBody CreateUserDTO dto) {
        UserDTO created = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PreAuthorize("hasRole('ADMIN') or authentication.principal == #id")
    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> update(
            @Positive @PathVariable Long id,
            @Valid @RequestBody UserDTO dto) {
        UserDTO updated = userService.update(dto);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@Positive @PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/activate/{id}")
    public ResponseEntity<Void> activate(@Positive @PathVariable Long id) {
        userService.activate(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivate(@Positive @PathVariable Long id) {
        userService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN') or authentication.principal == #id")
    @GetMapping("/get-cards-users-by-id/{id}")
    public ResponseEntity<List<PaymentCardsDTO>> getCardsById(@Positive @PathVariable Long id) {
        List<PaymentCardsDTO> cards = userService.getCardsByUserId(id);
        return ResponseEntity.ok(cards);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String surname,
            Pageable pageable) {

        Page<UserDTO> users = userService.getAll(name, surname, pageable);
        return ResponseEntity.ok(users);
    }
}
