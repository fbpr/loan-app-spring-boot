package com.enigmacamp.loan_app.controller;

import com.enigmacamp.loan_app.constant.ERole;
import com.enigmacamp.loan_app.entity.AppUser;
import com.enigmacamp.loan_app.entity.Role;
import com.enigmacamp.loan_app.model.response.AppUserResponse;
import com.enigmacamp.loan_app.service.AppUserService;
import com.enigmacamp.loan_app.service.AuthService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AppUserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AppUserService appUserService;

    @InjectMocks
    private AppUserController appUserController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(appUserController).build();
    }

    @Test
    void shouldReturnUserWhenGetUserById() throws Exception {
        String userId = "1";
        Role mockRole = new Role("1", ERole.ROLE_CUSTOMER);
        AppUserResponse response = new AppUserResponse("test@test.com", List.of(mockRole.getRole().getDescription()));

        when(appUserService.getUserById(userId)).thenReturn(response);

        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("test@test.com"))
                .andExpect(jsonPath("$.data.role").isArray())
                .andExpect(jsonPath("$.data.role", containsInAnyOrder("customer")));

        verify(appUserService, times(1)).getUserById(userId);
    }
}
