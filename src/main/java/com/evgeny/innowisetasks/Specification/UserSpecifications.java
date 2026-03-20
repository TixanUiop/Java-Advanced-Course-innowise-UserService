package com.evgeny.innowisetasks.Specification;

import com.evgeny.innowisetasks.Entity.UserEntity;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    private UserSpecifications() {}

    public static Specification<UserEntity> hasFirstName(String firstName) {
        return (root, query, cb) -> firstName == null ? null : cb.like(root.get("firstName"), "%" + firstName + "%");
    }

    public static Specification<UserEntity> hasLastName(String lastName) {
        return (root, query, cb) -> lastName == null ? null : cb.like(root.get("lastName"), "%" + lastName + "%");
    }
}
