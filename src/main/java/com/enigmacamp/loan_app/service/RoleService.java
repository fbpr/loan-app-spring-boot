package com.enigmacamp.loan_app.service;

import com.enigmacamp.loan_app.constant.ERole;

public interface RoleService {
    void create(ERole role);
    String getById(String id);
}
