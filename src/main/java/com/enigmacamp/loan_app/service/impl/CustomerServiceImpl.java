package com.enigmacamp.loan_app.service.impl;

import com.enigmacamp.loan_app.entity.Customer;
import com.enigmacamp.loan_app.model.response.CustomerResponse;
import com.enigmacamp.loan_app.repository.CustomerRepository;
import com.enigmacamp.loan_app.service.CustomerService;
import com.enigmacamp.loan_app.specification.CustomerSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponse getById(String id) {
        Customer customer = findByIdOrElseThrow(id);
        return CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .dateOfBirth(customer.getDateOfBirth())
                .phone(customer.getPhone())
                .status(customer.getStatus())
                .build();
    }

    @Override
    public List<CustomerResponse> getAll() {
        Specification<Customer> specification = CustomerSpecification.getSpecification();
        return customerRepository.findAll(specification).stream().map(customer -> CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .dateOfBirth(customer.getDateOfBirth())
                .phone(customer.getPhone())
                .status(customer.getStatus())
                .build()).toList();
    }

    @Override
    public CustomerResponse update(Customer customer) {
        findByIdOrElseThrow(customer.getId());
        Customer updatedCustomer = customerRepository.saveAndFlush(customer);
        
        return CustomerResponse.builder()
                .id(updatedCustomer.getId())
                .firstName(updatedCustomer.getFirstName())
                .lastName(updatedCustomer.getLastName())
                .dateOfBirth(updatedCustomer.getDateOfBirth())
                .phone(updatedCustomer.getPhone())
                .status(updatedCustomer.getStatus())
                .build();
    }

    @Override
    public void softDelete(String id) {
        Customer customer = findByIdOrElseThrow(id);
        customer.setIsDeleted(true);
        customer.setDeletedAt(Instant.now().toEpochMilli());
        customerRepository.saveAndFlush(customer);
    }

    public Customer findByIdOrElseThrow(String id) {
        return customerRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }
}

