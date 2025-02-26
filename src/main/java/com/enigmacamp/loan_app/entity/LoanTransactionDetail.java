package com.enigmacamp.loan_app.entity;

import com.enigmacamp.loan_app.constant.LoanStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "trx_loan_detail")
public class LoanTransactionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "transaction_date")
    private Long transactionDate;
    @Column(name = "nominal")
    private Double nominal;
    @ManyToOne
    @JoinColumn(name = "loan_transaction_id")
    private LoanTransaction loanTransaction;
    @Enumerated(EnumType.STRING)
    @Column(name = "loan_status")
    private LoanStatus loanStatus; // enum
    @Column(name = "created_at")
    private Long createdAt;
    @Column(name = "updated_at")
    private Long updatedAt;
}
