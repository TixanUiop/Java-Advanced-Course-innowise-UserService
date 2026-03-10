package com.evgeny.innowisetasks.Service;


import com.evgeny.innowisetasks.DTO.CreateUserDTO;
import com.evgeny.innowisetasks.DTO.PaymentCardsDTO;
import com.evgeny.innowisetasks.DTO.UserDTO;
import com.evgeny.innowisetasks.Entity.UserEntity;
import com.evgeny.innowisetasks.Mapper.UserMapper;
import com.evgeny.innowisetasks.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public UserDTO delete(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
        return userMapper.toDTO(user);
    }

    @Override
    @Transactional
    public UserDTO create(CreateUserDTO dto) {
        UserEntity user = userMapper.toEntity(dto);
        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public UserDTO getById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDTO(user);
    }

    @Override
    public Page<UserDTO> getAll(String name, String surname, Pageable pageable) {
        Page<UserEntity> users = userRepository.findByNameAndSurnameNative(name, surname, pageable);
        return users.map(userMapper::toDTO);
    }

    @Override
    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());
        user.setActive(dto.getActive());

        return userMapper.toDTO(user);
    }

    @Override
    @Transactional
    public Boolean activate(Long id) {
        int updated = userRepository.updateActiveStatusJPQL(id, true);

        if (updated == 0) {
            throw new RuntimeException("Status change error");
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean deactivate(Long id) {
        int updated = userRepository.updateActiveStatusJPQL(id, false);

        if (updated == 0) {
            throw new RuntimeException("Status change error");
        }
        return true;
    }

    @Override
    public List<PaymentCardsDTO> getCardsByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getCards().stream()
                .map(card -> new PaymentCardsDTO(card.getId(), card.getNumber(), card.getHolder(), card.getExpirationDate(), card.getActive()))
                .collect(Collectors.toList());
    }
}
