package com.enigmacamp.loan_app.model.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanTransactionResponse {
    private String id;
    private String loanTypeId;
    private String instalmentTypeId;
    private String customerId;
    private Long nominal;
    private Long approvedAt;
    private String approvedBy;
    private String approvalStatus;
    private List<LoanTransactionDetailResponse> transactionDetailResponses;
    private Long createdAt;
    private Long updatedAt;
}
