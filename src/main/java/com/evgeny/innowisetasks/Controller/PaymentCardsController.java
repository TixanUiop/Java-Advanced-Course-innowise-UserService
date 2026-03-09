package com.evgeny.innowisetasks.Controller;

import com.evgeny.innowisetasks.DTO.CreatePaymentCardsDTO;
import com.evgeny.innowisetasks.DTO.PaymentCardsDTO;
import com.evgeny.innowisetasks.DTO.UserDTO;
import com.evgeny.innowisetasks.Service.PaymentCardService;
import com.evgeny.innowisetasks.Service.PaymentCardServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/payment-cards")
public class PaymentCardsController {

    public PaymentCardService paymentCardService;

    @Autowired
    public PaymentCardsController(PaymentCardServiceImpl paymentCardService)
    {
        this.paymentCardService = paymentCardService;
    }

    @GetMapping
    public Page<PaymentCardsDTO> getAllPaymentCards(
            Pageable pageable
    ) {
        return paymentCardService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public Optional<PaymentCardsDTO> getPaymentCardsById(@PathVariable Long id)
    {
        return Optional.of(paymentCardService.getById(id));
    }

    @PostMapping("/create")
    public Optional<PaymentCardsDTO> create(@Valid @RequestBody CreatePaymentCardsDTO dto) {
        return Optional.of(paymentCardService.create(dto));
    }

    @PutMapping("/update")
    public PaymentCardsDTO update(
            @Valid @RequestBody PaymentCardsDTO dto) {

        return paymentCardService.update(dto);
    }

    @DeleteMapping("/delete/{id}")
    public PaymentCardsDTO delete(@PathVariable Long id) {
        return paymentCardService.delete(id);
    }

    @PostMapping("/activate/{id}")
    public Boolean activate(@PathVariable Long id) {
        return paymentCardService.activate(id);
    }

    @PostMapping("/deactivate/{id}")
    public Boolean deactivate(@PathVariable Long id) {
        return paymentCardService.deactivate(id);
    }

}
