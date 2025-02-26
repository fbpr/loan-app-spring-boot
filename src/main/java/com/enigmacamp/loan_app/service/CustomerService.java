package com.enigmacamp.loan_app.service;

import com.enigmacamp.loan_app.entity.Customer;
import com.enigmacamp.loan_app.model.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CustomerResponse getById(String id);
    List<CustomerResponse> getAll();
    CustomerResponse update(Customer customer);
    void softDelete(String id);
}
