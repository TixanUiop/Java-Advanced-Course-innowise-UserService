package com.evgeny.innowisetasks.Controller;

import com.evgeny.innowisetasks.DTO.PaymentCardsDTO;
import com.evgeny.innowisetasks.DTO.UserDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/payment-cards")
public class PaymentCardsController {

    @GetMapping
    public List<PaymentCardsDTO> getAllPaymentCards() {
        return null; //get all
    }

    @GetMapping("/{id}")
    public Optional<PaymentCardsDTO> getPaymentCardsById(Long id)
    {
        return Optional.empty(); //get user by id
    }

    @PostMapping("/create")
    public Optional<PaymentCardsDTO> create(@Valid @RequestBody PaymentCardsDTO dto) {
        return null; //create user
    }

    @PutMapping("/update")
    public PaymentCardsDTO update(
            @Valid @RequestBody UserDTO dto) {

        return null; //update user
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        //delete user
    }

}
