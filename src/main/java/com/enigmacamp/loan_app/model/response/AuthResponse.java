package com.enigmacamp.loan_app.model.response;

import com.enigmacamp.loan_app.entity.Role;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String email;
    private List<String> role;
    private String token;
}
