package com.evgeny.innowisetasks.Controller;


import com.evgeny.innowisetasks.DTO.UserDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return null; //get all
    }

    @GetMapping("/user/{id}")
    public Optional<UserDTO> getUserById(Long id)
    {
        return Optional.empty(); //get user by id
    }

    @PostMapping
    public Optional<UserDTO> create(@Valid @RequestBody UserDTO dto) {
        return null; //create user
    }

    @PutMapping("/user/update/{id}")
    public UserDTO update(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO dto) {

        return null; //update user
    }

    @DeleteMapping("/user/delete/{id}")
    public void delete(@PathVariable Long id) {
         //delete user
    }

}
