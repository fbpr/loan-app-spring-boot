package com.enigmacamp.loan_app.service;

import com.enigmacamp.loan_app.model.request.LoanTransactionRequest;
import com.enigmacamp.loan_app.model.response.LoanTransactionResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LoanTransactionService {
    LoanTransactionResponse create(LoanTransactionRequest request);
    LoanTransactionResponse getById(String id);
    LoanTransactionResponse approve(String adminId, LoanTransactionRequest request);
    void pay(String trxId, MultipartFile receiptFile);
    String saveReceipt(MultipartFile file);
}
