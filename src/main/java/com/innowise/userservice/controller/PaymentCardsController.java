

package com.innowise.userservice.controller;

import com.innowise.userservice.dto.CreatePaymentCardsDTO;
import com.innowise.userservice.dto.PaymentCardsDTO;
import com.innowise.userservice.service.PaymentCardService;
import com.innowise.userservice.service.PaymentCardServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<PaymentCardsDTO>> getAllPaymentCards(Pageable pageable) {
        Page<PaymentCardsDTO> cards = paymentCardService.getAll(pageable);
        return ResponseEntity.ok(cards);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentCardsDTO> getPaymentCardsById(@Positive @PathVariable Long id) {
        PaymentCardsDTO dto = paymentCardService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<PaymentCardsDTO> create(@Valid @RequestBody CreatePaymentCardsDTO dto) {
        PaymentCardsDTO created = paymentCardService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<PaymentCardsDTO> update(@Valid @RequestBody PaymentCardsDTO dto) {
        PaymentCardsDTO updated = paymentCardService.update(dto);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@Positive @PathVariable Long id) {
        paymentCardService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/activate/{id}")
    public ResponseEntity<Void> activate(@Positive @PathVariable Long id) {
        paymentCardService.activate(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivate(@Positive @PathVariable Long id) {
        paymentCardService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
