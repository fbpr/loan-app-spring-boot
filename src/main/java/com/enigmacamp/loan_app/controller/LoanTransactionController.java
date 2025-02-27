package com.enigmacamp.loan_app.controller;

import com.enigmacamp.loan_app.entity.LoanTransaction;
import com.enigmacamp.loan_app.model.request.LoanTransactionRequest;
import com.enigmacamp.loan_app.model.response.CommonResponse;
import com.enigmacamp.loan_app.model.response.LoanTransactionResponse;
import com.enigmacamp.loan_app.service.LoanTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class LoanTransactionController {
    private final LoanTransactionService loanTransactionService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CommonResponse<LoanTransactionResponse>> requestLoan(@RequestBody LoanTransactionRequest request) {
        LoanTransactionResponse newTransaction = loanTransactionService.create(request);
        CommonResponse<LoanTransactionResponse> response = CommonResponse.<LoanTransactionResponse>builder()
                .message("loan transaction created successfully")
                .data(newTransaction)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<LoanTransactionResponse>> getLoanTransactionById(@PathVariable String id) {
        LoanTransactionResponse transaction = loanTransactionService.getById(id);
        CommonResponse<LoanTransactionResponse> response = CommonResponse.<LoanTransactionResponse>builder()
                .message("loan transaction retrieved by id successfully")
                .data(transaction)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{adminId}/approve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<CommonResponse<LoanTransactionResponse>> approveLoan(@PathVariable String adminId, @RequestBody LoanTransactionRequest request) {
        LoanTransactionResponse approvedTransaction = loanTransactionService.approve(adminId, request);
        CommonResponse<LoanTransactionResponse> response = CommonResponse.<LoanTransactionResponse>builder()
                .message("loan transaction approved successfully")
                .data(approvedTransaction)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{trxId}/pay", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse<LoanTransactionResponse>> payInstalment(
            @PathVariable String trxId, @RequestParam("receipt") MultipartFile receipt) {
        loanTransactionService.pay(trxId, receipt);

        CommonResponse<LoanTransactionResponse> response = CommonResponse.<LoanTransactionResponse>builder()
                .message("loan instalment paid successfully")
                .build();

        return ResponseEntity.ok(response);
    }
}
