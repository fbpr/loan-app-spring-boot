package com.enigmacamp.loan_app.service;

import com.enigmacamp.loan_app.model.response.AppUserResponse;

public interface AppUserService {
    AppUserResponse getUserById(String id);
}
