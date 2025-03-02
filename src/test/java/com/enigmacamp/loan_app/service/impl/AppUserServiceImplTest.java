package com.enigmacamp.loan_app.service.impl;

import com.enigmacamp.loan_app.constant.ERole;
import com.enigmacamp.loan_app.entity.AppUser;
import com.enigmacamp.loan_app.entity.Customer;
import com.enigmacamp.loan_app.entity.Role;
import com.enigmacamp.loan_app.model.request.AuthRequest;
import com.enigmacamp.loan_app.model.response.AppUserResponse;
import com.enigmacamp.loan_app.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppUserServiceImplTest {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private AppUserServiceImpl appUserService;

    private AppUser user;

    @BeforeEach
    void setUp() {
        Role roles = Role.builder()
                .id("1")
                .role(ERole.ROLE_STAFF)
                .build();

        user = AppUser.builder()
                .id("1")
                .email("test@test.com")
                .password("test")
                .roles(List.of(roles))
                .build();
    }

    @Test
    void shouldReturnUserWhenIdExists() {
        when(appUserRepository.findById("1")).thenReturn(Optional.of(user));

        AppUserResponse foundUser = appUserService.getUserById("1");

        assertNotNull(foundUser);
        assertEquals("test@test.com", foundUser.getEmail());
        assertEquals(List.of(ERole.ROLE_STAFF.getDescription()), foundUser.getRole());
    }
}
