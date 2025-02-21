package com.enigmacamp.loan_app.service;

import com.enigmacamp.loan_app.entity.InstalmentType;
import com.enigmacamp.loan_app.model.request.InstalmentTypeRequest;

import java.util.List;

public interface InstalmentTypeService {
    InstalmentType create(InstalmentTypeRequest request);
    InstalmentType getById(String id);
    List<InstalmentType> getAll();
    InstalmentType update(InstalmentType type);
    void delete(String id);
}
