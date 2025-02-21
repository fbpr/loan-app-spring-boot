package com.enigmacamp.loan_app.service;

import com.enigmacamp.loan_app.model.request.LoanTransactionRequest;
import com.enigmacamp.loan_app.model.response.LoanTransactionResponse;

import java.util.List;

public interface LoanTransactionService {
    LoanTransactionResponse create(LoanTransactionRequest request);
    List<LoanTransactionResponse> getAll();
}
