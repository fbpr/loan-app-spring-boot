package com.enigmacamp.loan_app.config;

import com.enigmacamp.loan_app.constant.ERole;
import com.enigmacamp.loan_app.entity.Role;
import com.enigmacamp.loan_app.repository.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByRole(ERole.ROLE_ADMIN).isEmpty()) {
                Role admin = Role.builder().role(ERole.ROLE_ADMIN).build();
                roleRepository.save(admin);
            }
            if (roleRepository.findByRole(ERole.ROLE_STAFF).isEmpty()) {
                Role staff = Role.builder().role(ERole.ROLE_STAFF).build();
                roleRepository.save(staff);
            }
            if (roleRepository.findByRole(ERole.ROLE_CUSTOMER).isEmpty()) {
                Role customer = Role.builder().role(ERole.ROLE_CUSTOMER).build();
                roleRepository.save(customer);
            }
        };
    }

}
