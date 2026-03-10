package com.evgeny.innowisetasks.Controller;


import com.evgeny.innowisetasks.DTO.CreateUserDTO;
import com.evgeny.innowisetasks.DTO.PaymentCardsDTO;
import com.evgeny.innowisetasks.DTO.UserDTO;
import com.evgeny.innowisetasks.Service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public Page<UserDTO> getAllUsers(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String surname,
            Pageable pageable) {
        return userService.getAll(name, surname, pageable);
    }

    //Fix problem more then have
    @GetMapping("/{id}")
    public Optional<UserDTO> getUserById(@Positive @PathVariable Long id)
    {
        return Optional.of(userService.getById(id));
    }

    @PostMapping("/create")
    public Optional<UserDTO> create(@Valid @RequestBody CreateUserDTO dto) {
       return Optional.of(userService.create(dto));
    }

    @PutMapping("/update/{id}")
    public UserDTO update(
            @Positive @PathVariable Long id,
            @Valid @RequestBody UserDTO dto) {

        return userService.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public UserDTO delete(@Positive @PathVariable Long id) {
        return userService.delete(id);
    }

    @PostMapping("/activate/{id}")
    public Boolean activate(@Positive @PathVariable Long id) {
        return userService.activate(id);
    }

    @PostMapping("/deactivate/{id}")
    public Boolean deactivate(@Positive @PathVariable Long id) {
        return userService.deactivate(id);
    }

    @GetMapping("/get-cards-users-by-id/{id}")
    public List<PaymentCardsDTO> getCardsById(@Positive @PathVariable Long id) {
        return userService.getCardsByUserId(id);
    }

}
