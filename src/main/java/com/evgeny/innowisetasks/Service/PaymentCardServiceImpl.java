package com.evgeny.innowisetasks.Service;


import com.evgeny.innowisetasks.DTO.CreatePaymentCardsDTO;
import com.evgeny.innowisetasks.DTO.PaymentCardsDTO;
import com.evgeny.innowisetasks.DTO.UserDTO;
import com.evgeny.innowisetasks.Entity.PaymentCardsEntity;
import com.evgeny.innowisetasks.Entity.UserEntity;
import com.evgeny.innowisetasks.Exception.CardLimitExceededException;
import com.evgeny.innowisetasks.Exception.CardNotFoundException;
import com.evgeny.innowisetasks.Exception.CardStatusChangeException;
import com.evgeny.innowisetasks.Exception.UserNotFoundException;
import com.evgeny.innowisetasks.Mapper.PaymentCardsMapper;
import com.evgeny.innowisetasks.Repository.PaymentCardsRepository;
import com.evgeny.innowisetasks.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
            @Qualifier("paymentCardsMapperImpl") PaymentCardsMapper mapper) {
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.mapper = mapper;
    }


    @Override
    @Transactional
    @CacheEvict(value = "user_cards", key = "#dto.userId")
    public PaymentCardsDTO create(CreatePaymentCardsDTO dto) {

        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserId()));


        if (user.getCards().size() >= 5) {
            throw new CardLimitExceededException(dto.getUserId());
        }
        //TODO: Add check number
        PaymentCardsEntity card = mapper.createToEntity(dto);
        card.setHolder(user.getName() + " " + user.getSurname());

        card.setUser(user);
        user.getCards().add(card);

        return mapper.toDTO(card);
    }

    @Override
    @Cacheable(value = "cards", key = "#id")
    public PaymentCardsDTO getById(Long id) {
        PaymentCardsEntity card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        return mapper.toDTO(card);
    }

    @Override
    public Page<PaymentCardsDTO> getAll(Pageable pageable) {
        Page<PaymentCardsEntity> cards = cardRepository.findAll(pageable);
        return cards.map(
                card -> mapper.toDTO(card)
        );
    }

    @Override
    @Cacheable(value = "user_cards", key = "#userId")
    public List<PaymentCardsDTO> getAllByUserId(Long userId) {
        return cardRepository.findAllByUserId(userId).stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    @CacheEvict(value = {"cards", "user_cards"}, allEntries = false, key = "#dto.id")
    public PaymentCardsDTO update(PaymentCardsDTO dto) {
        PaymentCardsEntity card = cardRepository.findById(dto.getId())
                .orElseThrow(() -> new CardNotFoundException(dto.getId()));

        card.setNumber(dto.getNumber());
        card.setExpirationDate(dto.getExpirationDate());
        card.setActive(dto.getActive());

        return mapper.toDTO(card);
    }

    @Override
    @Transactional
    @CacheEvict(value = "user_cards", key = "#id")
    public Boolean activate(Long id) {
        int updated = cardRepository.updateActiveStatusPayJPQL(id, true);

        if (updated == 0) {
            throw new CardStatusChangeException(id);
        }
        return true;
    }

    @Override
    @Transactional
    @CacheEvict(value = "user_cards", key = "#id")
    public Boolean deactivate(Long id) {
        int updated = cardRepository.updateActiveStatusPayJPQL(id, false);

        if (updated == 0) {
            throw new CardStatusChangeException(id);
        }
        return true;
    }

    @Override
    @CacheEvict(value = {"cards", "user_cards"}, key = "#id")
    public PaymentCardsDTO delete(Long id) {
        PaymentCardsEntity card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));

        cardRepository.delete(card);
        return mapper.toDTO(card);
    }

}
