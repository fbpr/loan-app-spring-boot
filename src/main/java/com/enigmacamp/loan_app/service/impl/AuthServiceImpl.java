package com.enigmacamp.loan_app.service.impl;

import com.enigmacamp.loan_app.constant.ERole;
import com.enigmacamp.loan_app.entity.AppUser;
import com.enigmacamp.loan_app.entity.Role;
import com.enigmacamp.loan_app.model.request.AuthRequest;
import com.enigmacamp.loan_app.model.response.AuthResponse;
import com.enigmacamp.loan_app.model.response.SignupResponse;
import com.enigmacamp.loan_app.repository.AppUserRepository;
import com.enigmacamp.loan_app.repository.RoleRepository;
import com.enigmacamp.loan_app.security.JwtAuthenticationFilter;
import com.enigmacamp.loan_app.security.JwtTokenProvider;
import com.enigmacamp.loan_app.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationManager authenticationManager;

    @Override
    public SignupResponse createAdmin(AuthRequest request) {
        try {
            List<Role> userRole;
            Role adminRole = roleRepository.findByRole(ERole.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role role = Role.builder().role(ERole.ROLE_ADMIN).build();
                        return roleRepository.save(role);
                    });

            Role staffRole = roleRepository.findByRole(ERole.ROLE_STAFF)
                    .orElseGet(() -> {
                        Role role = Role.builder().role(ERole.ROLE_STAFF).build();
                        return roleRepository.save(role);
            });

            userRole = List.of(adminRole, staffRole);

            AppUser user = AppUser.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .roles(userRole)
                    .build();

            appUserRepository.saveAndFlush(user);
            return toSignupResponse(user);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email Duplicate");
        }
    }

    @Override
    public SignupResponse createStaff(AuthRequest request) {
        return null;
    }

    @Override
    public SignupResponse createCustomer(AuthRequest request) {
        return null;
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                            request.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            String email = userDetails.getUsername();

            String token = jwtTokenProvider.generateToken(userDetails.getUsername(), roles);

            return toLoginResponse(email, token, roles);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    private static SignupResponse toSignupResponse(AppUser user) {
        return SignupResponse.builder()
                .email(user.getEmail())
                .role(user.getRoles().stream().map(role -> role.getRole().getDescription()).toList())
                .build();
    }

    private static AuthResponse toLoginResponse(String email, String token, List<String> roles) {
        return AuthResponse.builder()
                .email(email)
                .token(token)
                .role(roles)
                .build();
    }
}
