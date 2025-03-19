package com.enigmacamp.loan_app.controller;

import com.enigmacamp.loan_app.constant.EInstalmentType;
import com.enigmacamp.loan_app.entity.InstalmentType;
import com.enigmacamp.loan_app.model.request.InstalmentTypeRequest;
import com.enigmacamp.loan_app.service.InstalmentTypeService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class InstalmentTypeControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private InstalmentTypeService instalmentTypeService;

    @InjectMocks
    private InstalmentTypeController instalmentTypeController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(instalmentTypeController).build();
    }

    @Test
    void shouldReturnInstalmentTypeWhenCreated() throws Exception {
        InstalmentTypeRequest newInstalmentType = InstalmentTypeRequest.builder()
                .instalmentType("ONE_MONTH")
                .build();

        InstalmentType response = InstalmentType.builder()
                .id("1")
                .instalmentType(EInstalmentType.ONE_MONTH)
                .build();

        when(instalmentTypeService.create(any(InstalmentTypeRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/instalment-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newInstalmentType)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("instalment type created successfully"))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.instalmentType").value("ONE_MONTH"));

        verify(instalmentTypeService, times(1)).create(any(InstalmentTypeRequest.class));
    }

    @Test
    void shouldReturnInstalmentTypeResponseWhenGetById() throws Exception {
        String instalmentTypeId = "1";

        InstalmentType response = InstalmentType.builder()
                .id("1")
                .instalmentType(EInstalmentType.ONE_MONTH)
                .build();

        when(instalmentTypeService.getById(instalmentTypeId)).thenReturn(response);

        mockMvc.perform(get("/api/v1/instalment-types/{id}", instalmentTypeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("instalment type retrieved by id successfully"))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.instalmentType").value("ONE_MONTH"));

        verify(instalmentTypeService, times(1)).getById(instalmentTypeId);
    }

    @Test
    void shouldReturnInstalmentTypesResponseWhenGetInstalmentTypes() throws Exception {
        InstalmentType instalmentType1 = InstalmentType.builder()
                .id("1")
                .instalmentType(EInstalmentType.ONE_MONTH)
                .build();

        InstalmentType instalmentType2 = InstalmentType.builder()
                .id("2")
                .instalmentType(EInstalmentType.THREE_MONTHS)
                .build();

        when(instalmentTypeService.getAll()).thenReturn(List.of(instalmentType1, instalmentType2));

        mockMvc.perform(get("/api/v1/instalment-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("instalment types retrieved successfully"))
                .andExpect(jsonPath("$.data.size()").value(2))
                .andExpect(jsonPath("$.data[0].id").value("1"))
                .andExpect(jsonPath("$.data[1].id").value("2"));

        verify(instalmentTypeService, times(1)).getAll();
    }

    @Test
    void shouldReturnUpdateInstalmentTypeWhenUpdateInstalmentType() throws Exception {
        InstalmentType updateInstalmentType = InstalmentType.builder()
                .id("1")
                .instalmentType(EInstalmentType.TWELVE_MONTHS)
                .build();

        when(instalmentTypeService.update(any(InstalmentType.class))).thenReturn(updateInstalmentType);

        mockMvc.perform(put("/api/v1/instalment-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateInstalmentType)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("instalment type updated successfully"))
                .andExpect(jsonPath("$.data.instalmentType").value("TWELVE_MONTHS"));

        verify(instalmentTypeService, times(1)).update(any(InstalmentType.class));
    }

    @Test
    void shouldDeletedWhenDeleteLoanType() throws Exception {
        String instalmentTypeId = "1";

        doNothing().when(instalmentTypeService).delete(instalmentTypeId);

        mockMvc.perform(delete("/api/v1/instalment-types/{id}", instalmentTypeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("instalment type deleted successfully"));

        verify(instalmentTypeService, times(1)).delete(instalmentTypeId);
    }
}
