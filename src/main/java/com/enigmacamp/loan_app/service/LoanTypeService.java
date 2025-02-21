package com.enigmacamp.loan_app.service;

import com.enigmacamp.loan_app.entity.LoanType;
import com.enigmacamp.loan_app.model.request.LoanTypeRequest;

import java.util.List;

public interface LoanTypeService {
    LoanType create(LoanTypeRequest request);
    LoanType getById(String id);
    List<LoanType> getAll();
    LoanType update(LoanType type);
    void delete(String id);
}
