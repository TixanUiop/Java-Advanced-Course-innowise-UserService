package com.evgeny.innowisetasks.Service;


import com.evgeny.innowisetasks.DTO.CreatePaymentCardsDTO;
import com.evgeny.innowisetasks.DTO.PaymentCardsDTO;
import com.evgeny.innowisetasks.Entity.PaymentCardsEntity;
import com.evgeny.innowisetasks.Entity.UserEntity;
import com.evgeny.innowisetasks.Mapper.PaymentCardsMapper;
import com.evgeny.innowisetasks.Repository.PaymentCardsRepository;
import com.evgeny.innowisetasks.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class PaymentCardServiceImpl implements PaymentCardService {

    private final UserRepository userRepository;
    private final PaymentCardsRepository cardRepository;
    private final PaymentCardsMapper mapper;

    @Autowired
    public PaymentCardServiceImpl(
            UserRepository userRepository,
            PaymentCardsRepository cardRepository,
            PaymentCardsMapper mapper) {
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.mapper = mapper;
    }


    @Override
    @Transactional
    public PaymentCardsDTO create(CreatePaymentCardsDTO dto, Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getCards().size() >= 5) {
            throw new RuntimeException("User cannot have more than 5 cards");
        }

        PaymentCardsEntity card = mapper.createToEntity(dto);
        card.setUser(user);
        user.getCards().add(card);

        return mapper.toDTO(card);
    }

    @Override
    public PaymentCardsDTO getById(Long id) {
        PaymentCardsEntity card = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        return mapper.toDTO(card);
    }

    @Override
    public Page<PaymentCardsDTO> getAll(Pageable pageable, String name, String surname) {
        Page<UserEntity> users = userRepository.findByNameAndSurnameNative(name, surname, pageable);
        return users.map(
                user -> (PaymentCardsDTO) mapper.toDtoList(user.getCards())
        );
    }

    @Override
    public List<PaymentCardsDTO> getAllByUserId(Long userId) {
        return cardRepository.findAllByUserId(userId).stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public PaymentCardsDTO update(Long id, PaymentCardsDTO dto) {
        PaymentCardsEntity card = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        card.setNumber(dto.getNumber());
        card.setExpirationDate(dto.getExpirationDate());
        card.setActive(dto.getActive());

        return mapper.toDTO(card);
    }

    @Override
    @Transactional
    public Boolean activate(Long id) {
        return cardRepository.updateActiveStatusJPQL(id, true);
    }

    @Override
    @Transactional
    public Boolean deactivate(Long id) {
        return cardRepository.updateActiveStatusJPQL(id, false);
    }

}
