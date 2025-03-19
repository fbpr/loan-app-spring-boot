package com.enigmacamp.loan_app.service.impl;

import com.enigmacamp.loan_app.entity.Customer;
import com.enigmacamp.loan_app.model.response.CustomerResponse;
import com.enigmacamp.loan_app.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.enigmacamp.loan_app.specification.CustomerSpecification;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id("1")
                .firstName("Test")
                .lastName("Customer")
                .phone("123456789")
                .dateOfBirth(Date.valueOf("2025-02-28"))
                .status("1")
                .isDeleted(false)
                .build();

    }

    @Test
    void shouldReturnCustomerWhenIdExists() {
        when(customerRepository.findByIdAndIsDeletedFalse("1")).thenReturn(Optional.of(customer));

        CustomerResponse foundCustomer = customerService.getById("1");

        assertNotNull(foundCustomer);
        assertEquals("Test", foundCustomer.getFirstName());
        assertEquals("Customer", foundCustomer.getLastName());
        assertEquals("123456789", foundCustomer.getPhone());
        assertEquals("1", foundCustomer.getStatus());
        assertEquals(Date.valueOf("2025-02-28"), foundCustomer.getDateOfBirth());
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        when(customerRepository.findByIdAndIsDeletedFalse("1")).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> customerService.getById("1"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Customer not found", exception.getReason());
    }

    @Test
    void shouldReturnCustomersWhenGetAll() {
        List<Customer> customerList = List.of(customer);

        when(customerRepository.findAll(ArgumentMatchers.<Specification<Customer>>any())).thenReturn(customerList);

        List<CustomerResponse> customers = customerService.getAll();

        assertNotNull(customers);
        assertEquals(customerList.size(), customers.size());
        assertEquals("Test", customers.get(0).getFirstName());
        assertEquals("Customer", customers.get(0).getLastName());
    }

    @Test
    void shouldUpdateCustomerSuccessfully() {
        when(customerRepository.findByIdAndIsDeletedFalse("1")).thenReturn(Optional.of(customer));
        when(customerRepository.saveAndFlush(any(Customer.class))).thenReturn(customer);

        customer.setFirstName("Updated");

        CustomerResponse updateCustomer = customerService.update(customer);

        assertNotNull(updateCustomer);
        assertEquals("Updated", updateCustomer.getFirstName());
        assertEquals("Customer", updateCustomer.getLastName());

        verify(customerRepository, times(1)).findByIdAndIsDeletedFalse("1");
        verify(customerRepository, times(1)).saveAndFlush(any(Customer.class));
    }

    @Test
    void shouldSoftDeleteCustomerWhenIdExists() {
        when(customerRepository.findByIdAndIsDeletedFalse("1")).thenReturn(Optional.of(customer));
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        doReturn(customer).when(customerRepository).saveAndFlush(customerCaptor.capture());
        customerService.softDelete("1");

        Customer captured = customerCaptor.getValue();
        assertTrue(captured.getIsDeleted());
        assertTrue(captured.getDeletedAt() > 0L);

        verify(customerRepository, times(1)).findByIdAndIsDeletedFalse("1");
        verify(customerRepository, times(1)).saveAndFlush(any(Customer.class));
    }
}
