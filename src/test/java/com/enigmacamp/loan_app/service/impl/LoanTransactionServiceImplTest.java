package com.enigmacamp.loan_app.service.impl;

import com.enigmacamp.loan_app.constant.ApprovalStatus;
import com.enigmacamp.loan_app.constant.EInstalmentType;
import com.enigmacamp.loan_app.constant.LoanStatus;
import com.enigmacamp.loan_app.entity.*;
import com.enigmacamp.loan_app.model.request.LoanTransactionRequest;
import com.enigmacamp.loan_app.model.response.LoanTransactionResponse;
import com.enigmacamp.loan_app.repository.LoanTransactionRepository;
import com.enigmacamp.loan_app.repository.LoanTypeRepository;
import com.enigmacamp.loan_app.service.LoanTransactionDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Path;
import java.sql.Date;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanTransactionServiceImplTest {

    @TempDir
    Path tempDir;

    @Mock
    private LoanTransactionRepository loanTransactionRepository;

    @Mock
    private LoanTransactionDetailService loanTransactionDetailService;

    @InjectMocks
    private LoanTransactionServiceImpl loanTransactionService;

    private LoanType loanType;
    private InstalmentType instalmentType;
    private Customer customer;
    private MultipartFile mockFile;
    private LoanTransaction loanTransaction;
    private LoanTransactionDetail loanTransactionDetail;
    private LoanTransactionRequest request;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id("1")
                .firstName("Test")
                .lastName("Customer")
                .dateOfBirth(Date.valueOf("2025-02-28"))
                .phone("123456789")
                .status("1")
                .isDeleted(false)
                .build();

        loanType = LoanType.builder()
                .id("1")
                .type("Test Loan Type")
                .maxLoan(100.0)
                .build();

        instalmentType = InstalmentType.builder()
                .id("1")
                .instalmentType(EInstalmentType.ONE_MONTH)
                .build();

        loanTransaction = LoanTransaction.builder()
                .id("trx-1")
                .customer(customer)
                .loanType(loanType)
                .instalmentType(instalmentType)
                .nominal(1000.0)
                .createdAt(Instant.now().toEpochMilli())
                .approvalStatus(ApprovalStatus.APPROVED)
                .loanTransactionDetails(Collections.emptyList())
                .build();

        loanTransactionDetail = LoanTransactionDetail.builder()
                .id("trx-detail-1")
                .transactionDate(Instant.now().toEpochMilli())
                .nominal(1300.0)
                .loanStatus(LoanStatus.PAID)
                .createdAt(Instant.now().toEpochMilli())
                .updatedAt(Instant.now().toEpochMilli())
                .build();

        request = LoanTransactionRequest.builder()
                .loanType(loanType)
                .instalmentType(instalmentType)
                .customer(customer)
                .nominal(1000.0)
                .build();
    }

    @Test
    void shouldCreateLoanTransactionSuccessfully() {
        when(loanTransactionRepository.saveAndFlush(any(LoanTransaction.class))).thenReturn(loanTransaction);

        LoanTransactionResponse createLoanTransaction = loanTransactionService.create(request);

        assertNotNull(createLoanTransaction);
        assertEquals(customer.getId(), createLoanTransaction.getCustomerId());
        assertEquals(loanType.getId(), createLoanTransaction.getLoanTypeId());
        assertEquals(instalmentType.getId(), createLoanTransaction.getInstalmentTypeId());

        verify(loanTransactionRepository, times(1)).saveAndFlush(any(LoanTransaction.class));
    }

    @Test
    void shouldReturnLoanTransactionWhenIdExists() {
        loanTransaction.setLoanTransactionDetails(List.of(loanTransactionDetail));
        when(loanTransactionRepository.findById("1")).thenReturn(Optional.of(loanTransaction));

        LoanTransactionResponse foundLoanTransaction = loanTransactionService.getById("1");

        assertNotNull(foundLoanTransaction);
        assertEquals(loanType.getId(), foundLoanTransaction.getLoanTypeId());
        assertEquals(1, foundLoanTransaction.getTransactionDetailResponses().size());
    }

    @Test
    void shouldThrowExceptionWhenLoanTransactionNotFound() {;
        when(loanTransactionRepository.findById("1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> loanTransactionService.getById("1"));
    }

    @Test
    void shouldThrowExceptionWhenNoUnpaidDetailsExist() {
        LoanTransactionDetail paidDetail = LoanTransactionDetail.builder()
                .id("trx-detail-1")
                .loanStatus(LoanStatus.PAID)
                .build();
        loanTransaction.setLoanTransactionDetails(List.of(paidDetail));

        when(loanTransactionRepository.findById("trx-1")).thenReturn(Optional.of(loanTransaction));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            loanTransactionService.pay("trx-1", mockFile);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("No unpaid transaction detail found", exception.getReason());
    }

    @Test
    void shouldReturnLoanTrasanctionWithDetailWhenApproved() {
        String adminId = "2";
        LoanTransactionRequest requestApprove = LoanTransactionRequest.builder()
                .loanTransactionId("trx-1")
                .interestRates(3)
                .build();

        when(loanTransactionRepository.findById("trx-1")).thenReturn(Optional.of(loanTransaction));
        when(loanTransactionRepository.saveAndFlush(any(LoanTransaction.class))).thenReturn(loanTransaction);
        doNothing().when(loanTransactionDetailService).createBulk(anyList());

        LoanTransactionResponse approvedLoan = loanTransactionService.approve(adminId, requestApprove);

        assertNotNull(approvedLoan);
        assertEquals(1030, approvedLoan.getTransactionDetailResponses().get(0).getNominal());

        verify(loanTransactionRepository, times(1)).saveAndFlush(any(LoanTransaction.class));
        verify(loanTransactionDetailService, times(1)).createBulk(anyList());
    }

    @Test
    void shouldUpdateTransactionDetailWhenValidPayment() throws Exception {
        ReflectionTestUtils.setField(loanTransactionService, "UPLOAD_DIR", tempDir.toString());

        LoanTransactionDetail unpaidDetail = LoanTransactionDetail.builder()
                .id("trx-detail-2")
                .loanStatus(LoanStatus.UNPAID)
                .build();
        loanTransaction.setLoanTransactionDetails(List.of(unpaidDetail));

        when(loanTransactionRepository.findById("trx-1")).thenReturn(Optional.of(loanTransaction));
        mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn("test.txt");
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream("test".getBytes()));
        doNothing().when(loanTransactionDetailService).update(any(LoanTransactionDetail.class));
        
        loanTransactionService.pay("trx-1", mockFile);

        ArgumentCaptor<LoanTransactionDetail> trxDetailCaptor = ArgumentCaptor.forClass(LoanTransactionDetail.class);
        verify(loanTransactionDetailService).update(trxDetailCaptor.capture());

        LoanTransactionDetail updatedDetail = trxDetailCaptor.getValue();
        assertEquals(LoanStatus.PAID, updatedDetail.getLoanStatus());
        assertNotNull(updatedDetail.getTransactionDate());
        assertNotNull(updatedDetail.getUpdatedAt());
    }

    @Test
    void shouldSaveReceiptSuccessfully() throws Exception {
        mockFile = mock(MultipartFile.class);

        ReflectionTestUtils.setField(loanTransactionService, "UPLOAD_DIR", tempDir.toString());
        when(mockFile.getOriginalFilename()).thenReturn("test.txt");
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream("test".getBytes()));

        String savedReceipt = loanTransactionService.saveReceipt(mockFile);

        assertNotNull(savedReceipt);
        assertTrue(savedReceipt.contains("test.txt"));

        File savedFile = new File(savedReceipt);
        assertTrue(savedFile.exists());
    }

    @Test
    void shouldThrowExceptionWhenFailedSaveReceipt () {
        mockFile = mock(MultipartFile.class);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            loanTransactionService.saveReceipt(mockFile);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Failed to save receipt", exception.getReason());
    }

    @Test
    void shouldThrowExceptionWhenReceiptIsEmpty () {
        mockFile = mock(MultipartFile.class);

        LoanTransactionDetail unpaidDetail = LoanTransactionDetail.builder()
                .id("trx-detail-2")
                .loanStatus(LoanStatus.UNPAID)
                .build();
        loanTransaction.setLoanTransactionDetails(List.of(unpaidDetail));

        when(loanTransactionRepository.findById("trx-1")).thenReturn(Optional.of(loanTransaction));
        when(mockFile.isEmpty()).thenReturn(true);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            loanTransactionService.pay("trx-1", mockFile);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("receipt is required", exception.getReason());
    }

    @Test
    void shouldThrowExceptionWhenReceiptIsNull() {
        LoanTransactionDetail unpaidDetail = LoanTransactionDetail.builder()
                .id("trx-detail-2")
                .loanStatus(LoanStatus.UNPAID)
                .build();
        loanTransaction.setLoanTransactionDetails(List.of(unpaidDetail));

        when(loanTransactionRepository.findById("trx-1")).thenReturn(Optional.of(loanTransaction));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            loanTransactionService.pay("trx-1", null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("receipt is required", exception.getReason());
    }
}
