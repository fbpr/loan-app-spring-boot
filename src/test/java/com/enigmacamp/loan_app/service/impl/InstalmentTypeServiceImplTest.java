package com.enigmacamp.loan_app.service.impl;

import com.enigmacamp.loan_app.constant.EInstalmentType;
import com.enigmacamp.loan_app.entity.InstalmentType;
import com.enigmacamp.loan_app.model.request.InstalmentTypeRequest;
import com.enigmacamp.loan_app.repository.InstalmentTypeRepository;
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
class InstalmentTypeServiceImplTest {

    @Mock
    private InstalmentTypeRepository instalmentTypeRepository;

    @InjectMocks
    private InstalmentTypeServiceImpl instalmentTypeService;

    private InstalmentType instalmentType;
    private InstalmentTypeRequest request;

    @BeforeEach
    void setUp() {
        instalmentType = InstalmentType.builder()
                .id("1")
                .instalmentType(EInstalmentType.ONE_MONTH)
                .build();

        request = InstalmentTypeRequest.builder()
                .instalmentType(EInstalmentType.ONE_MONTH.name())
                .build();
    }

    @Test
    void shouldCreateInstalmentTypeSuccessfully() {
        when(instalmentTypeRepository.saveAndFlush(any(InstalmentType.class))).thenReturn(instalmentType);

        InstalmentType createInstalmentType = instalmentTypeService.create(request);

        assertNotNull(createInstalmentType);
        assertEquals(EInstalmentType.ONE_MONTH.name(), createInstalmentType.getInstalmentType().name());

        verify(instalmentTypeRepository, times(1)).saveAndFlush(any(InstalmentType.class));
    }

    @Test
    void shouldReturnInstalmentTypeWhenIdExists() {
        when(instalmentTypeRepository.findById("1")).thenReturn(Optional.of(instalmentType));

        InstalmentType foundInstalmentType = instalmentTypeService.getById("1");

        assertNotNull(foundInstalmentType);
        assertEquals(EInstalmentType.ONE_MONTH.name(), foundInstalmentType.getInstalmentType().name());
    }

    @Test
    void shouldThrowExceptionWhenInstalmentTypeNotFound() {
        when(instalmentTypeRepository.findById("1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> instalmentTypeService.getById("1"));
    }

    @Test
    void shouldReturnInstalmentTypeWhenGetAllSuccessfully() {
        when(instalmentTypeRepository.findAll()).thenReturn(List.of(instalmentType));

        List<InstalmentType> instalmentTypeList = instalmentTypeService.getAll();

        assertNotNull(instalmentTypeList);
        assertEquals(1, instalmentTypeList.size());

        verify(instalmentTypeRepository, times(1)).findAll();
    }

    @Test
    void shouldUpdateInstalmentTypeSuccessfully() {
        when(instalmentTypeRepository.findById("1")).thenReturn(Optional.of(instalmentType));
        when(instalmentTypeRepository.saveAndFlush(any(InstalmentType.class))).thenReturn(instalmentType);

        instalmentType.setInstalmentType(EInstalmentType.THREE_MONTHS);

        InstalmentType updateInstalmentType = instalmentTypeService.update(instalmentType);

        assertNotNull(updateInstalmentType);
        assertEquals(EInstalmentType.THREE_MONTHS.name(), updateInstalmentType.getInstalmentType().name());

        verify(instalmentTypeRepository, times(1)).findById("1");
        verify(instalmentTypeRepository, times(1)).saveAndFlush(any(InstalmentType.class));
    }

    @Test
    void shouldDeleteInstalmentTypeSuccessfully() {
        when(instalmentTypeRepository.findById("1")).thenReturn(Optional.of(instalmentType));
        doNothing().when(instalmentTypeRepository).delete(instalmentType);

        instalmentTypeService.delete("1");

        verify(instalmentTypeRepository, times(1)).delete(instalmentType);
    }
}
