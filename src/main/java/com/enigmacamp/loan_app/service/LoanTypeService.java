package com.enigmacamp.loan_app.service;

import com.enigmacamp.loan_app.entity.LoanType;
import com.enigmacamp.loan_app.model.request.LoanTypeRequest;
import com.enigmacamp.loan_app.model.response.LoanResponse;

import java.util.List;

public interface LoanTypeService {
    LoanResponse create(LoanTypeRequest request);
    LoanResponse getById(String id);
    List<LoanResponse> getAll();
    LoanResponse update(LoanType type);
    void delete(String id);
}
