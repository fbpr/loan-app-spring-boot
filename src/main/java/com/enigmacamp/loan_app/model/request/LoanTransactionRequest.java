package com.enigmacamp.loan_app.model.request;

import com.enigmacamp.loan_app.entity.Customer;
import com.enigmacamp.loan_app.entity.InstalmentType;
import com.enigmacamp.loan_app.entity.LoanType;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanTransactionRequest {
    private String loanTransactionId;
    private LoanType loanType;
    private InstalmentType instalmentType;
    private Customer customer;
    private Double nominal;
    private Integer interestRates;
}
