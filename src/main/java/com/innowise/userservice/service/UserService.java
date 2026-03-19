package com.innowise.innowisetasks.service;

import com.innowise.innowisetasks.dto.CreateUserDTO;
import com.innowise.innowisetasks.dto.PaymentCardsDTO;
import com.innowise.innowisetasks.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserDTO delete(Long id);

    UserDTO create(CreateUserDTO dto);

    UserDTO getById(Long id);

    Page<UserDTO> getAll(String name, String surname, Pageable pageable);

    UserDTO update(UserDTO dto);

    Boolean activate(Long id);

    Boolean deactivate(Long id);

    List<PaymentCardsDTO> getCardsByUserId(Long userId);
}
