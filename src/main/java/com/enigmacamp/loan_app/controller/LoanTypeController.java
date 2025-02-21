package com.enigmacamp.loan_app.controller;

import com.enigmacamp.loan_app.entity.LoanType;
import com.enigmacamp.loan_app.entity.LoanType;
import com.enigmacamp.loan_app.model.request.LoanTypeRequest;
import com.enigmacamp.loan_app.model.response.CommonResponse;
import com.enigmacamp.loan_app.service.LoanTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/loan-types")
public class LoanTypeController {
    private final LoanTypeService loanTypeService;
    
    @PostMapping
    public ResponseEntity<CommonResponse<LoanType>> createLoanType(@RequestBody LoanTypeRequest request) {
        LoanType newType = loanTypeService.create(request);
        CommonResponse<LoanType> response = CommonResponse.<LoanType>builder()
                .message("loan type created successfully")
                .data(newType)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<LoanType>> getLoanTypeById(@PathVariable String id) {
        LoanType type = loanTypeService.getById(id);
        CommonResponse<LoanType> response = CommonResponse.<LoanType>builder()
                .message("loan type retrieved by id successfully")
                .data(type)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<LoanType>>> getAllLoanTypes() {
        List<LoanType> types = loanTypeService.getAll();
        CommonResponse<List<LoanType>> response =
                CommonResponse.<List<LoanType>>builder()
                        .message("loan types retrieved successfully")
                        .data(types)
                        .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<CommonResponse<LoanType>> updateLoanType(@RequestBody LoanType type) {
        LoanType updatedLoanType = loanTypeService.update(type);
        CommonResponse<LoanType> response =
                CommonResponse.<LoanType>builder()
                        .message("loan type updated successfully")
                        .data(updatedLoanType)
                        .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<LoanType>> deleteLoanType(@PathVariable String id) {
        loanTypeService.delete(id);
        CommonResponse<LoanType> response = CommonResponse.<LoanType>builder()
                .message("loan type deleted successfully")
                .build();

        return ResponseEntity.ok(response);
    }
}
