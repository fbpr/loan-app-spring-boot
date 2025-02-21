package com.enigmacamp.loan_app.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanTypeRequest {
    private String type;
    private Double maxLoan;
}
