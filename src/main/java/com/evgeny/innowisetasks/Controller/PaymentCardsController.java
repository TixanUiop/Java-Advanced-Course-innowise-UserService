package com.evgeny.innowisetasks.Controller;

import com.evgeny.innowisetasks.DTO.CreatePaymentCardsDTO;
import com.evgeny.innowisetasks.DTO.PaymentCardsDTO;
import com.evgeny.innowisetasks.DTO.UserDTO;
import com.evgeny.innowisetasks.Service.PaymentCardService;
import com.evgeny.innowisetasks.Service.PaymentCardServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/payment-cards")
@Validated
public class PaymentCardsController {

    public PaymentCardService paymentCardService;

    @Autowired
    public PaymentCardsController(PaymentCardServiceImpl paymentCardService)
    {
        this.paymentCardService = paymentCardService;
    }

    @GetMapping
    public ResponseEntity<Page<PaymentCardsDTO>> getAllPaymentCards(Pageable pageable) {
        Page<PaymentCardsDTO> cards = paymentCardService.getAll(pageable);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentCardsDTO> getPaymentCardsById(@Positive @PathVariable Long id) {
        PaymentCardsDTO dto = paymentCardService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/create")
    public ResponseEntity<PaymentCardsDTO> create(@Valid @RequestBody CreatePaymentCardsDTO dto) {
        PaymentCardsDTO created = paymentCardService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/update")
    public ResponseEntity<PaymentCardsDTO> update(@Valid @RequestBody PaymentCardsDTO dto) {
        PaymentCardsDTO updated = paymentCardService.update(dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@Positive @PathVariable Long id) {
        paymentCardService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/activate/{id}")
    public ResponseEntity<Void> activate(@Positive @PathVariable Long id) {
        paymentCardService.activate(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivate(@Positive @PathVariable Long id) {
        paymentCardService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
