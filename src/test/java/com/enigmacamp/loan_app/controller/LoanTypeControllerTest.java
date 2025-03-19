package com.enigmacamp.loan_app.controller;

import com.enigmacamp.loan_app.entity.LoanType;
import com.enigmacamp.loan_app.model.request.LoanTypeRequest;
import com.enigmacamp.loan_app.model.response.LoanResponse;
import com.enigmacamp.loan_app.service.LoanTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LoanTypeControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private LoanTypeService loanTypeService;

    @InjectMocks
    private LoanTypeController loanTypeController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loanTypeController).build();
    }

    @Test
    void shouldReturnLoanTypeResponseWhenCreated() throws Exception {
        LoanType newLoanType = LoanType.builder()
                .type("Test Loan Type")
                .maxLoan(10000000.0)
                .build();

        LoanResponse response = LoanResponse.builder()
                .id("1")
                .type("Test Loan Type")
                .maxLoan(10000000L)
                .build();

        when(loanTypeService.create(any(LoanTypeRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/loan-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newLoanType)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("loan type created successfully"))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.type").value("Test Loan Type"))
                .andExpect(jsonPath("$.data.maxLoan").value(10000000L));

        verify(loanTypeService, times(1)).create(any(LoanTypeRequest.class));
    }

    @Test
    void shouldReturnLoanTypeResponseWhenGetById() throws Exception {
        String loanTypeId = "1";

        LoanResponse response = LoanResponse.builder()
                .id("1")
                .type("Test Loan Type")
                .maxLoan(10000000L)
                .build();

        when(loanTypeService.getById(loanTypeId)).thenReturn(response);

        mockMvc.perform(get("/api/v1/loan-types/{id}", loanTypeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("loan type retrieved by id successfully"))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.type").value("Test Loan Type"))
                .andExpect(jsonPath("$.data.maxLoan").value(10000000L));

        verify(loanTypeService, times(1)).getById(loanTypeId);
    }

    @Test
    void shouldReturnLoanTypesResponseWhenGetLoanTypes() throws Exception {
        LoanType loanType1 = LoanType.builder()
                .id("1")
                .type("Test Loan Type 1")
                .maxLoan(10000000.0)
                .build();

        LoanType loanType2 = LoanType.builder()
                .id("2")
                .type("Test Loan Type 2 ")
                .maxLoan(20000000.0)
                .build();

        List<LoanResponse> responseList = Stream.of(loanType1, loanType2).map(loanType -> LoanResponse.builder()
                .id(loanType.getId())
                .type(loanType.getType())
                .maxLoan(loanType.getMaxLoan().longValue())
                .build()).toList();

        when(loanTypeService.getAll()).thenReturn(responseList);

        mockMvc.perform(get("/api/v1/loan-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("loan types retrieved successfully"))
                .andExpect(jsonPath("$.data.size()").value(2))
                .andExpect(jsonPath("$.data[0].id").value("1"))
                .andExpect(jsonPath("$.data[1].id").value("2"));

        verify(loanTypeService, times(1)).getAll();
    }

    @Test
    void shouldReturnUpdateLoanTypeWhenUpdateLoanType() throws Exception {
        LoanType updateLoanType = LoanType.builder()
                .id("1")
                .type("Update Loan Type")
                .maxLoan(15000000.0)
                .build();

        LoanResponse response = LoanResponse.builder()
                .id("1")
                .type("Update Loan Type")
                .maxLoan(15000000L)
                .build();

        when(loanTypeService.update(any(LoanType.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/loan-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateLoanType)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("loan type updated successfully"))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.type").value("Update Loan Type"))
                .andExpect(jsonPath("$.data.maxLoan").value(15000000L));

        verify(loanTypeService, times(1)).update(any(LoanType.class));
    }

    @Test
    void shouldDeletedWhenDeleteLoanType() throws Exception {
        String loanTypeId = "1";

        doNothing().when(loanTypeService).delete(loanTypeId);

        mockMvc.perform(delete("/api/v1/loan-types/{id}", loanTypeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("loan type deleted successfully"));

        verify(loanTypeService, times(1)).delete(loanTypeId);
    }
}
