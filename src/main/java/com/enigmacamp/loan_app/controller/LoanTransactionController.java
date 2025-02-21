package com.enigmacamp.loan_app.controller;

import com.enigmacamp.loan_app.service.LoanTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class LoanTransactionController {
    private final LoanTransactionService loanTransactionService;
}
