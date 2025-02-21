package com.enigmacamp.loan_app.repository;

import com.enigmacamp.loan_app.entity.LoanTransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanTransactionDetailRepository extends JpaRepository<LoanTransactionDetail, String> {
}
