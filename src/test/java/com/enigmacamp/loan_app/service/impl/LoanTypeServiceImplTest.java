package com.enigmacamp.loan_app.service.impl;

import com.enigmacamp.loan_app.entity.LoanType;
import com.enigmacamp.loan_app.model.request.LoanTypeRequest;
import com.enigmacamp.loan_app.model.response.LoanResponse;
import com.enigmacamp.loan_app.repository.LoanTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanTypeServiceImplTest {

    @Mock
    private LoanTypeRepository loanTypeRepository;

    @InjectMocks
    private LoanTypeServiceImpl loanTypeService;

    private LoanType loanType;
    private LoanTypeRequest request;

    @BeforeEach
    void setUp() {
        loanType = LoanType.builder()
                .id("1")
                .type("Pinjaman Kredit Elektronik")
                .maxLoan(10000000.0)
                .build();

        request = LoanTypeRequest.builder()
                .type("Pinjaman Kredit Elektronik")
                .maxLoan(10000000.0)
                .build();
    }

    @Test
    void shouldCreateLoanTypeSuccessfully() {
        when(loanTypeRepository.saveAndFlush(any(LoanType.class))).thenReturn(loanType);

        LoanResponse createLoanType = loanTypeService.create(request);

        assertNotNull(createLoanType);
        assertEquals(loanType.getType(), createLoanType.getType());
        assertEquals(loanType.getMaxLoan(), createLoanType.getMaxLoan().doubleValue());

        verify(loanTypeRepository, times(1)).saveAndFlush(any(LoanType.class));
    }

    @Test
    void shouldReturnLoanTypeWhenIdExists() {
        when(loanTypeRepository.findById("1")).thenReturn(Optional.of(loanType));

        LoanResponse foundLoanType = loanTypeService.getById("1");

        assertNotNull(foundLoanType);
        assertEquals(loanType.getType(), foundLoanType.getType());
        assertEquals(loanType.getMaxLoan(), foundLoanType.getMaxLoan().doubleValue());
    }

    @Test
    void shouldThrowExceptionWhenLoanTypeNotFound() {
        when(loanTypeRepository.findById("1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> loanTypeService.getById("1"));
    }

    @Test
    void shouldReturnLoanTypeWhenGetAllSuccessfully() {
        when(loanTypeRepository.findAll()).thenReturn(List.of(loanType));

        List<LoanResponse> loanTypeList = loanTypeService.getAll();

        assertNotNull(loanTypeList);
        assertEquals(1, loanTypeList.size());
        assertEquals(loanType.getType(), loanTypeList.get(0).getType());
        assertEquals(loanType.getMaxLoan(), loanTypeList.get(0).getMaxLoan().doubleValue());
    }

    @Test
    void shouldUpdateLoanTypeSuccessfully() {
        when(loanTypeRepository.findById("1")).thenReturn(Optional.of(loanType));
        when(loanTypeRepository.saveAndFlush(any(LoanType.class))).thenReturn(loanType);

        loanType.setType("Pinjaman Kredit Bangunan");
        loanType.setMaxLoan(15000000.0);

        LoanResponse updateLoanType = loanTypeService.update(loanType);

        assertNotNull(updateLoanType);
        assertEquals(loanType.getType(), updateLoanType.getType());
        assertEquals(loanType.getMaxLoan(), updateLoanType.getMaxLoan().doubleValue());

        verify(loanTypeRepository, times(1)).findById("1");
        verify(loanTypeRepository, times(1)).saveAndFlush(any(LoanType.class));
    }

    @Test
    void shouldDeleteLoanTypeSuccessfully() {
        when(loanTypeRepository.findById("1")).thenReturn(Optional.of(loanType));
        doNothing().when(loanTypeRepository).delete(any(LoanType.class));

        loanTypeService.delete("1");

        verify(loanTypeRepository, times(1)).delete(any(LoanType.class));
    }

}
