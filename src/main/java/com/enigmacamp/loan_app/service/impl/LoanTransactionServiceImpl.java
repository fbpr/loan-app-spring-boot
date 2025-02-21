package com.enigmacamp.loan_app.service.impl;

import com.enigmacamp.loan_app.model.request.LoanTransactionRequest;
import com.enigmacamp.loan_app.model.response.LoanTransactionResponse;
import com.enigmacamp.loan_app.repository.LoanTransactionRepository;
import com.enigmacamp.loan_app.service.LoanTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanTransactionServiceImpl implements LoanTransactionService {
    private final LoanTransactionRepository loanTransactionRepository;


    @Override
    public LoanTransactionResponse create(LoanTransactionRequest request) {
        return null;
    }

    @Override
    public List<LoanTransactionResponse> getAll() {
        return List.of();
    }
}
