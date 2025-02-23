package com.enigmacamp.loan_app.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanTransactionDetailResponse {
    private String id;
    private Long transactionDate;
    private Long nominal;
    private String loanStatus;
    private Long createdAt;
    private Long updatedAt;
}
