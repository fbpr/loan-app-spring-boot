package com.enigmacamp.loan_app.service;

import com.enigmacamp.loan_app.entity.LoanTransactionDetail;

import java.util.List;

public interface LoanTransactionDetailService {
    void createBulk(List<LoanTransactionDetail> transactionDetails);
    void update(LoanTransactionDetail transactionDetail);
}
