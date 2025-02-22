package com.enigmacamp.loan_app.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanResponse {
    private String id;
    private String type;
    private Long maxLoan;
}
