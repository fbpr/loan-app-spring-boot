package com.enigmacamp.loan_app.service;

import com.enigmacamp.loan_app.model.request.AuthRequest;
import com.enigmacamp.loan_app.model.response.AuthResponse;
import com.enigmacamp.loan_app.model.response.SignupResponse;

public interface AuthService {
    SignupResponse createAdmin(AuthRequest request);
    SignupResponse createStaff(AuthRequest request);
    SignupResponse createCustomer(AuthRequest request);
    AuthResponse login (AuthRequest request);
//    LogoutResponse logout(HttpServletRequest request);
}
