package com.enigmacamp.loan_app.service;

import com.enigmacamp.loan_app.entity.LoanTransactionDetail;

import java.util.List;

public interface LoanTransactionDetailService {
    List<LoanTransactionDetail> createBulk(List<LoanTransactionDetail> transactionDetails);
}
