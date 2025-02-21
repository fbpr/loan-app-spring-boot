package com.enigmacamp.loan_app.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "t_loan_type")
public class LoanType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "type")
    private String type;
    @Column(name = "max_loan")
    private Double maxLoan;
}
