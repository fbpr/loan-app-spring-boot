package com.enigmacamp.loan_app.service.impl;

import com.enigmacamp.loan_app.entity.Customer;
import com.enigmacamp.loan_app.model.request.CustomerRequest;
import com.enigmacamp.loan_app.repository.CustomerRepository;
import com.enigmacamp.loan_app.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Customer getById(String id) {
        return null;
    }

    @Override
    public List<Customer> getAll(CustomerRequest request) {
        return List.of();
    }

    @Override
    public Customer update(Customer customer) {
        return null;
    }
}
