package com.enigmacamp.loan_app.controller;

import com.enigmacamp.loan_app.entity.InstalmentType;
import com.enigmacamp.loan_app.model.request.InstalmentTypeRequest;
import com.enigmacamp.loan_app.model.response.CommonResponse;
import com.enigmacamp.loan_app.service.InstalmentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/instalment-types")
public class InstalmentTypeController {
    private final InstalmentTypeService instalmentTypeService;

    @PostMapping
    public ResponseEntity<CommonResponse<InstalmentType>> createInstalmentType(@RequestBody InstalmentTypeRequest request) {
        InstalmentType newType = instalmentTypeService.create(request);
        CommonResponse<InstalmentType> response = CommonResponse.<InstalmentType>builder()
                .message("instalment type created successfully")
                .data(newType)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<InstalmentType>> getInstalmentTypeById(@PathVariable String id) {
        InstalmentType type = instalmentTypeService.getById(id);
        CommonResponse<InstalmentType> response = CommonResponse.<InstalmentType>builder()
                .message("installment type retrieved by id successfully")
                .data(type)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<InstalmentType>>> getAllInstalmentTypes() {
        List<InstalmentType> types = instalmentTypeService.getAll();
        CommonResponse<List<InstalmentType>> response =
                CommonResponse.<List<InstalmentType>>builder()
                .message("installment types retrieved successfully")
                .data(types)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<CommonResponse<InstalmentType>> updateInstalmentType(@RequestBody InstalmentType type) {
        InstalmentType updatedInstalmentType = instalmentTypeService.update(type);
        CommonResponse<InstalmentType> response =
                CommonResponse.<InstalmentType>builder()
                        .message("installment type updated successfully")
                        .data(updatedInstalmentType)
                        .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<InstalmentType>> deleteInstalmentType(@PathVariable String id) {
        instalmentTypeService.delete(id);
        CommonResponse<InstalmentType> response = CommonResponse.<InstalmentType>builder()
                .message("installment type deleted successfully")
                .build();

        return ResponseEntity.ok(response);
    }
}
