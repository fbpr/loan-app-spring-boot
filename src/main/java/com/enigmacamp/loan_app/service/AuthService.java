package com.enigmacamp.loan_app.service;

import com.enigmacamp.loan_app.model.request.AuthRequest;
import com.enigmacamp.loan_app.model.response.AuthResponse;
import com.enigmacamp.loan_app.model.response.SignupResponse;

public interface AuthService {
    SignupResponse createUser(AuthRequest request);
    AuthResponse login (AuthRequest request);
//    LogoutResponse logout(HttpServletRequest request);
}
