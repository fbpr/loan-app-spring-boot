package com.enigmacamp.loan_app.controller;

import com.enigmacamp.loan_app.entity.Customer;
import com.enigmacamp.loan_app.model.response.CommonResponse;
import com.enigmacamp.loan_app.model.response.CustomerResponse;
import com.enigmacamp.loan_app.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<CustomerResponse>> getCustomerById(@PathVariable String id) {
        CustomerResponse customer = customerService.getById(id);
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .message("successfully fetch user by id")
                .data(customer)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> getCustomers() {
        List<CustomerResponse> customers = customerService.getAll();
        CommonResponse<List<CustomerResponse>> response = CommonResponse.<List<CustomerResponse>>builder()
                .message("successfully fetch user")
                .data(customers)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<CommonResponse<CustomerResponse>> updateCustomer(@RequestBody Customer customer) {
        CustomerResponse updatedCustomer = customerService.update(customer);
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .message("successfully update user")
                .data(updatedCustomer)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<String>> deleteCustomer(@PathVariable String id) {
        customerService.softDelete(id);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .message("successfully delete user")
                .build();

        return ResponseEntity.ok(response);
    }
}
