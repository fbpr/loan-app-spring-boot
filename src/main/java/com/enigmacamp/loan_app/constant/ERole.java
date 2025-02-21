package com.enigmacamp.loan_app.constant;

import lombok.Getter;

@Getter
public enum ERole {
    ROLE_CUSTOMER("customer"),
    ROLE_STAFF("staff"),
    ROLE_ADMIN("admin");

    private final String description;

    ERole(String description) {
        this.description = description;
    }
}
