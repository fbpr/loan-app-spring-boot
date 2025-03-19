package com.enigmacamp.loan_app.controller;

import com.enigmacamp.loan_app.constant.LoanStatus;
import com.enigmacamp.loan_app.entity.Customer;
import com.enigmacamp.loan_app.entity.InstalmentType;
import com.enigmacamp.loan_app.entity.LoanType;
import com.enigmacamp.loan_app.model.request.LoanTransactionRequest;
import com.enigmacamp.loan_app.model.response.LoanTransactionDetailResponse;
import com.enigmacamp.loan_app.model.response.LoanTransactionResponse;
import com.enigmacamp.loan_app.service.LoanTransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LoanTransactionControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private LoanTransactionService loanTransactionService;

    @InjectMocks
    private LoanTransactionController loanTransactionController;

    private LoanTransactionRequest transactionRequest;
    private LoanTransactionResponse transactionResponse;
    private LoanTransactionDetailResponse transactionDetailResponse;
    private LoanType loanType;
    private InstalmentType instalmentType;
    private Customer customer;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loanTransactionController).build();

        loanType = LoanType.builder()
                .id("loan-type-1")
                .build();

        instalmentType = InstalmentType.builder()
                .id("instalment-type-1")
                .build();

        customer = Customer.builder()
                .id("customer-1")
                .build();

        transactionRequest = LoanTransactionRequest.builder()
                .loanType(loanType)
                .instalmentType(instalmentType)
                .customer(customer)
                .nominal(1000000.0)
                .build();

        transactionResponse = LoanTransactionResponse.builder()
                .id("trx-1")
                .loanTypeId("loan-type-1")
                .instalmentTypeId("instalment-type-1")
                .customerId("customer-1")
                .nominal(1000000L)
                .createdAt(1661106557370L)
                .build();
    }

    @Test
    void shouldReturnLoanTransactionResponseWhenRequestLoan() throws Exception {
        when(loanTransactionService.create(any(LoanTransactionRequest.class))).thenReturn(transactionResponse);

        mockMvc.perform(post("/api/v1/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("loan transaction created successfully"))
                .andExpect(jsonPath("$.data.id").value("trx-1"))
                .andExpect(jsonPath("$.data.loanTypeId").value("loan-type-1"))
                .andExpect(jsonPath("$.data.instalmentTypeId").value("instalment-type-1"))
                .andExpect(jsonPath("$.data.customerId").value("customer-1"))
                .andExpect(jsonPath("$.data.nominal").value(1000000L))
                .andExpect(jsonPath("$.data.createdAt").value(1661106557370L));

        verify(loanTransactionService, times(1)).create(any(LoanTransactionRequest.class));
    }

    @Test
    void shouldReturnLoanTransactionResponseWhenGetById() throws Exception {
        String transactionId = "trx-1";

        transactionDetailResponse = LoanTransactionDetailResponse.builder()
                .id("trx-detail-1")
                .createdAt(1661106557400L)
                .build();

        transactionResponse.setTransactionDetailResponses(List.of(transactionDetailResponse));

        when(loanTransactionService.getById(transactionId)).thenReturn(transactionResponse);

        mockMvc.perform(get("/api/v1/transactions/{id}", transactionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("loan transaction retrieved by id successfully"))
                .andExpect(jsonPath("$.data.id").value("trx-1"))
                .andExpect(jsonPath("$.data.transactionDetailResponses.size()").value(1))
                .andExpect(jsonPath("$.data.transactionDetailResponses[0].id").value("trx-detail-1"))
                .andExpect(jsonPath("$.data.transactionDetailResponses[0].createdAt").value(1661106557400L));

        verify(loanTransactionService, times(1)).getById(transactionId);
    }

    @Test
    void shouldReturnLoanTransactionResponseWhenLoanApproved() throws Exception {
        String adminId = "admin-1";
        String transactionId = "trx-1";

        LoanTransactionRequest approveRequest = LoanTransactionRequest.builder()
                .loanTransactionId(transactionId)
                .interestRates(3)
                .build();

        transactionDetailResponse = LoanTransactionDetailResponse.builder()
                .id("trx-detail-1")
                .transactionDate(1661091574279L)
                .nominal(1030000L)
                .loanStatus(LoanStatus.PAID.name())
                .createdAt(1661002579786L)
                .updatedAt(1661091574307L)
                .build();

        transactionResponse.setTransactionDetailResponses(List.of(transactionDetailResponse));

        transactionResponse.setTransactionDetailResponses(List.of(transactionDetailResponse));
        when(loanTransactionService.approve(eq(adminId), any(LoanTransactionRequest.class))).thenReturn(transactionResponse);

        mockMvc.perform(put("/api/v1/transactions/{adminId}/approve", adminId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("loan transaction approved successfully"))
                .andExpect(jsonPath("$.data.transactionDetailResponses[0].id").value("trx-detail-1"))
                .andExpect(jsonPath("$.data.transactionDetailResponses[0].transactionDate").value(1661091574279L))
                .andExpect(jsonPath("$.data.transactionDetailResponses[0].nominal").value(1030000L))
                .andExpect(jsonPath("$.data.transactionDetailResponses[0].loanStatus").value("PAID"));

        verify(loanTransactionService, times(1)).approve(eq(adminId), any(LoanTransactionRequest.class));
    }

    @Test
    void shouldReturnMessageResponseWhenPayInstalment() throws Exception {
         String transactionId = "trx-1";
         MockMultipartFile receiptFile = new MockMultipartFile(
                         "receipt",
                         "receipt.jpg",
                         MediaType.IMAGE_JPEG_VALUE,
                         "receipt image content".getBytes()
         );

         doNothing().when(loanTransactionService).pay(eq(transactionId), any(MultipartFile.class));

         mockMvc.perform(multipart(HttpMethod.PUT, "/api/v1/transactions/{trxId}/pay", transactionId)
                         .file(receiptFile)
                         .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                         .andExpect(status().isOk())
                         .andExpect(jsonPath("$.message").value("loan instalment paid successfully"))
                         .andExpect(jsonPath("$.data").doesNotExist());

         verify(loanTransactionService, times(1)).pay(eq(transactionId), any(MultipartFile.class));
    }

}





