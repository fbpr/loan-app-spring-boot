package com.enigmacamp.loan_app.controller;

import com.enigmacamp.loan_app.model.response.AppUserResponse;
import com.enigmacamp.loan_app.model.response.CommonResponse;
import com.enigmacamp.loan_app.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<AppUserResponse>> getUserById(@PathVariable String id) {
        AppUserResponse user = appUserService.getUserById(id);
        CommonResponse<AppUserResponse> response = CommonResponse.<AppUserResponse>builder()
                .message("user fetched successfully")
                .data(user)
                .build();

        return ResponseEntity.ok(response);
    }

}
