package com.enigmacamp.loan_app.service;

import com.enigmacamp.loan_app.entity.Customer;
import com.enigmacamp.loan_app.model.request.CustomerRequest;

import java.util.List;

public interface CustomerService {
    Customer getById(String id);
    List<Customer> getAll(CustomerRequest request);
    Customer update(Customer customer);
}
