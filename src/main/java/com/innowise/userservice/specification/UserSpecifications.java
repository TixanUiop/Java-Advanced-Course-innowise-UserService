package com.innowise.innowisetasks.specification;

import com.innowise.innowisetasks.entity.UserEntity;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    private UserSpecifications() {}

    public static Specification<UserEntity> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null) {
                return cb.conjunction();
            }
            return cb.like(
                    cb.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%"
            );
        };
    }

    public static Specification<UserEntity> hasSurname(String surname) {
        return (root, query, cb) -> {
            if (surname == null) {
                return cb.conjunction();
            }
            return cb.like(
                    cb.lower(root.get("surname")),
                    "%" + surname.toLowerCase() + "%"
            );
        };
    }
}
