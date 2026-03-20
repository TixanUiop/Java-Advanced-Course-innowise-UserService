package com.evgeny.innowisetasks.Service;

import com.evgeny.innowisetasks.DTO.CreateUserDTO;
import com.evgeny.innowisetasks.DTO.PaymentCardsDTO;
import com.evgeny.innowisetasks.DTO.UserDTO;
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
