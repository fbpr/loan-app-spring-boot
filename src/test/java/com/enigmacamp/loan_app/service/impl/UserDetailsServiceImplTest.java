package com.enigmacamp.loan_app.service.impl;

import com.enigmacamp.loan_app.constant.ERole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.enigmacamp.loan_app.entity.AppUser;
import com.enigmacamp.loan_app.entity.Role;
import com.enigmacamp.loan_app.repository.AppUserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private AppUser user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = Role.builder()
                .id("1")
                .role(ERole.ROLE_CUSTOMER)
                .build();

        user = AppUser.builder()
                .id("1")
                .email("test@gmail.com")
                .password("test")
                .roles(List.of(role))
                .build();
    }

    @Test
    void shouldLoadUserByEmailSuccessfully() {
        when(appUserRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@gmail.com");

        assertNotNull(userDetails);
        assertEquals("test@gmail.com", userDetails.getUsername());
        assertThat(userDetails.getAuthorities()).extracting(GrantedAuthority::getAuthority).containsExactlyInAnyOrder("ROLE_CUSTOMER");
    }

    @Test
    void shouldThrowExceptionWhenLoadUserByEmailNotFound() {
        when(appUserRepository.findByEmail("test@gmail.com")).thenReturn(Optional.empty());
        assertThrowsExactly(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("test@gmail.com"));
    }
}

