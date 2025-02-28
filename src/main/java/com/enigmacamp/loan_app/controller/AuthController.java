package com.enigmacamp.loan_app.controller;

import com.enigmacamp.loan_app.constant.ApiBash;
import com.enigmacamp.loan_app.model.request.AuthRequest;
import com.enigmacamp.loan_app.model.response.AuthResponse;
import com.enigmacamp.loan_app.model.response.CommonResponse;
import com.enigmacamp.loan_app.model.response.SignupResponse;
import com.enigmacamp.loan_app.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiBash.AUTH_ENDPOINT)
public class AuthController {

    private final AuthService authService;

    @PostMapping(ApiBash.SIGNUP)
    public ResponseEntity<CommonResponse<SignupResponse>> registerUser(@RequestBody AuthRequest request) {
        SignupResponse newUser = authService.createUser(request);
        CommonResponse<SignupResponse> response = CommonResponse.<SignupResponse>builder()
                .message("successfully created user")
                .data(newUser)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(ApiBash.SIGNIN)
    public ResponseEntity<CommonResponse<AuthResponse>> login(@RequestBody AuthRequest request) {
        AuthResponse newLogin = authService.login(request);
        CommonResponse<AuthResponse> response = CommonResponse.<AuthResponse>builder()
                .message("successfully login")
                .data(newLogin)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
