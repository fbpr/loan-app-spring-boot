package com.enigmacamp.loan_app.service.impl;

import com.enigmacamp.loan_app.constant.ApprovalStatus;
import com.enigmacamp.loan_app.constant.LoanStatus;
import com.enigmacamp.loan_app.entity.LoanTransaction;
import com.enigmacamp.loan_app.entity.LoanTransactionDetail;
import com.enigmacamp.loan_app.model.request.LoanTransactionRequest;
import com.enigmacamp.loan_app.model.response.LoanTransactionDetailResponse;
import com.enigmacamp.loan_app.model.response.LoanTransactionResponse;
import com.enigmacamp.loan_app.repository.LoanTransactionRepository;
import com.enigmacamp.loan_app.service.LoanTransactionDetailService;
import com.enigmacamp.loan_app.service.LoanTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanTransactionServiceImpl implements LoanTransactionService {
    private final LoanTransactionRepository loanTransactionRepository;
    private final LoanTransactionDetailService loanTransactionDetailService;

    private String UPLOAD_DIR = "D:/BTB/uploads";

    @Override
    public LoanTransactionResponse create(LoanTransactionRequest request) {
        LoanTransaction transaction = LoanTransaction.builder()
                .loanType(request.getLoanType())
                .instalmentType(request.getInstalmentType())
                .customer(request.getCustomer())
                .nominal(request.getNominal())
                .createdAt(Instant.now().toEpochMilli())
                .build();
        loanTransactionRepository.saveAndFlush(transaction);

        return LoanTransactionResponse.builder()
                .id(transaction.getId())
                .loanTypeId(transaction.getLoanType().getId())
                .instalmentTypeId(transaction.getInstalmentType().getId())
                .customerId(transaction.getCustomer().getId())
                .nominal(transaction.getNominal().longValue())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

    @Override
    public LoanTransactionResponse getById(String id) {
        LoanTransaction transaction = findByIdOrElseThrow(id);
        return LoanTransactionResponse.builder()
                .id(transaction.getId())
                .loanTypeId(transaction.getLoanType().getId())
                .instalmentTypeId(transaction.getInstalmentType().getId())
                .customerId(transaction.getCustomer().getId())
                .nominal(transaction.getNominal().longValue())
                .createdAt(transaction.getCreatedAt())
                .approvedAt(transaction.getApprovedAt())
                .approvedBy(transaction.getApprovedBy())
                .approvalStatus(transaction.getApprovalStatus().name())
                .updatedAt(transaction.getUpdatedAt())
                .transactionDetailResponses(transaction.getLoanTransactionDetails().stream().map(detail -> {
                    return LoanTransactionDetailResponse.builder()
                            .id(detail.getId())
                            .transactionDate(detail.getTransactionDate())
                            .nominal(detail.getNominal().longValue())
                            .loanStatus(detail.getLoanStatus().name())
                            .createdAt(detail.getCreatedAt())
                            .updatedAt(detail.getUpdatedAt())
                            .build();
                }).toList())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoanTransactionResponse approve(String adminId, LoanTransactionRequest request) {
        LoanTransaction transaction = findByIdOrElseThrow(request.getLoanTransactionId());
        transaction.setApprovalStatus(ApprovalStatus.APPROVED);
        transaction.setApprovedAt(Instant.now().toEpochMilli());
        transaction.setApprovedBy("test@test.com");
        transaction.setUpdatedAt(Instant.now().toEpochMilli());
        loanTransactionRepository.saveAndFlush(transaction);

        List<LoanTransactionDetail> trxDetails = transaction.getLoanTransactionDetails();

        LoanTransactionDetail newTrxDetail = LoanTransactionDetail.builder()
                .loanTransaction(transaction)
                .transactionDate(Instant.now().toEpochMilli())
                .nominal(transaction.getNominal() + (transaction.getNominal() * request.getInterestRates() / 100))
                .loanStatus(LoanStatus.PAID)
                .createdAt(Instant.now().toEpochMilli())
                .updatedAt(Instant.now().toEpochMilli())
                .build();

        trxDetails.add(newTrxDetail);
        loanTransactionDetailService.createBulk(trxDetails);
        transaction.setLoanTransactionDetails(trxDetails);
        List<LoanTransactionDetailResponse> trxDetailResponses =
                trxDetails.stream().map(detail -> {
                    return LoanTransactionDetailResponse.builder()
                            .id(detail.getId())
                            .transactionDate(detail.getTransactionDate())
                            .nominal(detail.getNominal().longValue())
                            .loanStatus(detail.getLoanStatus().name())
                            .createdAt(detail.getCreatedAt())
                            .updatedAt(detail.getUpdatedAt())
                            .build();
                }).toList();

        return LoanTransactionResponse.builder()
                .id(transaction.getId())
                .loanTypeId(transaction.getLoanType().getId())
                .instalmentTypeId(transaction.getInstalmentType().getId())
                .customerId(transaction.getCustomer().getId())
                .nominal(transaction.getNominal().longValue())
                .createdAt(transaction.getCreatedAt())
                .approvedAt(transaction.getApprovedAt())
                .approvedBy(transaction.getApprovedBy())
                .approvalStatus(transaction.getApprovalStatus().name())
                .updatedAt(transaction.getUpdatedAt())
                .transactionDetailResponses(trxDetailResponses)
                .build();
    }

    @Override
    public void pay(String trxId, MultipartFile receiptFile) {
        LoanTransaction transaction = findByIdOrElseThrow(trxId);

        LoanTransactionDetail trxDetail = transaction.getLoanTransactionDetails().stream()
                .filter(detail -> detail.getLoanStatus() == LoanStatus.UNPAID)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No unpaid transaction detail found"));

        if (receiptFile != null && !receiptFile.isEmpty()) {
            saveReceipt(receiptFile);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "receipt is required");
        }

        trxDetail.setTransactionDate(Instant.now().toEpochMilli());
        trxDetail.setLoanStatus(LoanStatus.PAID);
        trxDetail.setUpdatedAt(Instant.now().toEpochMilli());
        loanTransactionDetailService.update(trxDetail);
    }

    @Override
    public String saveReceipt(MultipartFile file) {
        try {
            Files.createDirectories(Path.of(UPLOAD_DIR));

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);

            Files.copy(file.getInputStream(), filePath);

            return filePath.toString();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save receipt");
        }
    }

    public LoanTransaction findByIdOrElseThrow(String id) {
        return loanTransactionRepository.findById(id).orElseThrow(() -> new RuntimeException(
                "Loan Transaction" +
                        " not " +
                        "found"));
    }
}
