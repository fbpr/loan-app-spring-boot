package com.enigmacamp.loan_app.service.impl;

import com.enigmacamp.loan_app.entity.AppUser;
import com.enigmacamp.loan_app.model.response.AppUserResponse;
import com.enigmacamp.loan_app.repository.AppUserRepository;
import com.enigmacamp.loan_app.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;

    @Override
    public AppUserResponse getUserById(String id) {
        AppUser user = findByIdOrElseThrow(id);

        return AppUserResponse.builder()
                .email(user.getEmail())
                .role(user.getRoles().stream().map(role -> role.getRole().getDescription()).toList())
                .build();
    }

    public AppUser findByIdOrElseThrow(String id) {
        return appUserRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
