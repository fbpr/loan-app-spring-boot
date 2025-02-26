package com.enigmacamp.loan_app.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRequest {
    private String name;
    private String mobilePhone;
    private String birthDate;
    private Boolean status;
}
