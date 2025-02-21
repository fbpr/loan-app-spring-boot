package com.enigmacamp.loan_app.controller;

import com.enigmacamp.loan_app.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public void createCustomer() {

    }
}
