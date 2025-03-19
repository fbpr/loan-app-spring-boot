package com.enigmacamp.loan_app.controller;

import com.enigmacamp.loan_app.entity.Customer;
import com.enigmacamp.loan_app.model.response.CustomerResponse;
import com.enigmacamp.loan_app.repository.CustomerRepository;
import com.enigmacamp.loan_app.service.CustomerService;
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

import java.sql.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CustomerController(customerService)).build();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    void shouldReturnCustomerResponseWhenGetCustomerById() throws Exception {
        String customerId = "1";

        CustomerResponse response = CustomerResponse.builder()
                .id("1")
                .firstName("Test")
                .lastName("Customer")
                .dateOfBirth(Date.valueOf("2025-02-28"))
                .phone("123456789")
                .build();

        when(customerService.getById(customerId)).thenReturn(response);

        mockMvc.perform(get("/api/v1/customers/{id}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("successfully fetch user by id"))
                .andExpect(jsonPath("$.data.firstName").value("Test"))
                .andExpect(jsonPath("$.data.lastName").value("Customer"))
                .andExpect(jsonPath("$.data.dateOfBirth").value("2025-02-28"))
                .andExpect(jsonPath("$.data.phone").value("123456789"));

        verify(customerService, times(1)).getById(customerId);
    }

    @Test
    void shouldReturnCustomersResponseWhenGetCustomers() throws Exception {
        Customer customer1 = Customer.builder()
                .id("1")
                .firstName("Test")
                .lastName("Customer 1")
                .dateOfBirth(Date.valueOf("2025-02-28"))
                .phone("123456789")
                .build();

        Customer customer2 = Customer.builder()
                .id("2")
                .firstName("Test")
                .lastName("Customer 2")
                .dateOfBirth(Date.valueOf("2025-02-28"))
                .phone("987654321")
                .build();

        List<CustomerResponse> responseList = Stream.of(customer1, customer2).map(customer -> CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .dateOfBirth(customer.getDateOfBirth())
                .phone(customer.getPhone())
                .build()).toList();

        when(customerService.getAll()).thenReturn(responseList);

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("successfully fetch user"))
                .andExpect(jsonPath("$.data.size()").value(2))
                .andExpect(jsonPath("$.data[0].id").value("1"))
                .andExpect(jsonPath("$.data[1].id").value("2"));


        verify(customerService, times(1)).getAll();
    }

    @Test
    void shouldUpdateCustomerWhenUpdateCustomer() throws Exception {
        Customer updateCustomer = Customer.builder()
                .id("1")
                .firstName("Update")
                .lastName("Customer 1")
                .dateOfBirth(Date.valueOf("2025-02-28"))
                .phone("123456789")
                .status("0")
                .build();

        CustomerResponse response = CustomerResponse.builder()
                .id(updateCustomer.getId())
                .firstName(updateCustomer.getFirstName())
                .lastName(updateCustomer.getLastName())
                .dateOfBirth(updateCustomer.getDateOfBirth())
                .phone(updateCustomer.getPhone())
                .status(updateCustomer.getStatus())
                .build();

        when(customerService.update(any(Customer.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("successfully update user"))
                .andExpect(jsonPath("$.data.firstName").value("Update"))
                .andExpect(jsonPath("$.data.status").value("0"));


        verify(customerService, times(1)).update(any(Customer.class));
    }

    @Test
    void shouldSoftDeleteCustomerWhenDeleteCustomer() throws Exception {
        String customerId = "1";

        doNothing().when(customerService).softDelete(customerId);

        mockMvc.perform(delete("/api/v1/customers/{id}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("successfully delete user"));

        verify(customerService, times(1)).softDelete(customerId);
    }
}
