package com.innowise.innowisetasks.service;


import com.innowise.innowisetasks.dto.CreateUserDTO;
import com.innowise.innowisetasks.dto.PaymentCardsDTO;
import com.innowise.innowisetasks.dto.UserDTO;
import com.innowise.innowisetasks.entity.UserEntity;
import com.innowise.innowisetasks.exception.StatusChangeErrorException;
import com.innowise.innowisetasks.exception.UserAlreadyExistsException;
import com.innowise.innowisetasks.exception.UserNotFoundException;
import com.innowise.innowisetasks.mapper.UserMapper;
import com.innowise.innowisetasks.repository.UserRepository;
import com.innowise.innowisetasks.specification.UserSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @CacheEvict(value = "users", key = "#id")
    @Override
    @Transactional
    public UserDTO delete(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setActive(false);

        if (user.getCards() != null) {
            user.getCards().forEach(card -> card.setActive(false));
        }

        userRepository.save(user);

        return userMapper.toDTO(user);
    }

    @Override
    @Transactional
    @CachePut(value = "users", key = "#result.id")
    public UserDTO create(CreateUserDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException(dto.getEmail());
        }
        UserEntity user = userMapper.toEntity(dto);
        return userMapper.toDTO(userRepository.save(user));
    }

    @Cacheable(value = "users", key = "#id")
    @Override
    public UserDTO getById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toDTO(user);
    }

    @Override
    public Page<UserDTO> getAll(String name, String surname, Pageable pageable) {
        Specification<UserEntity> spec = Specification
                .where(UserSpecifications.hasName(name))
                .and(UserSpecifications.hasSurname(surname));

        Page<UserEntity> users = userRepository.findAll(spec, pageable);

        return users.map(userMapper::toDTO);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#dto")
    public UserDTO update(UserDTO dto) {

        UserEntity user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new UserNotFoundException(dto.getId()));

        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());
        user.setActive(dto.getActive());
        user = userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public Boolean activate(Long id) {
        int updated = userRepository.updateActiveStatusJPQL(id, true);

        if (updated == 0) {
            throw new StatusChangeErrorException();
        }
        return true;
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public Boolean deactivate(Long id) {
        int updated = userRepository.updateActiveStatusJPQL(id, false);

        if (updated == 0) {
            throw new StatusChangeErrorException();
        }
        return true;
    }

    @Cacheable(value = "user_cards", key = "#userId")
    @Override
    public List<PaymentCardsDTO> getCardsByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return user.getCards().stream()
                .map(card -> new PaymentCardsDTO(card.getId(), card.getNumber(), card.getHolder(), card.getExpirationDate(), card.getActive()))
                .collect(Collectors.toList());
    }
}
