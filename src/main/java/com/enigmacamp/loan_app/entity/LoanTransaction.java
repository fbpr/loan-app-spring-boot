package com.enigmacamp.loan_app.entity;

import com.enigmacamp.loan_app.constant.ApprovalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "trx_loan")
public class LoanTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne
    @JoinColumn(name = "loan_type_id")
    private LoanType loanType;
    @ManyToOne
    @JoinColumn(name = "instalment_type_id")
    private InstalmentType instalmentType;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @Column(name = "nominal")
    private Double nominal;
    @Column(name = "approved_at")
    private Long approvedAt;
    @Column(name = "approved_by")
    private String approvedBy;
    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private ApprovalStatus approvalStatus; // enum
    @OneToMany
    @JoinColumn(name = "loan_transaction_id")
    private List<LoanTransactionDetail> loanTransactionDetails;
    @Column(name = "created_at")
    private Long createdAt;
    @Column(name = "updated_at")
    private Long updatedAt;
}
