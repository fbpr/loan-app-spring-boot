package com.enigmacamp.loan_app.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstalmentTypeRequest {
    private String instalmentType;
}
