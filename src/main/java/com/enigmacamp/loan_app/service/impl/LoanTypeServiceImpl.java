package com.enigmacamp.loan_app.service.impl;

import com.enigmacamp.loan_app.entity.LoanType;
import com.enigmacamp.loan_app.model.request.LoanTypeRequest;
import com.enigmacamp.loan_app.repository.LoanTypeRepository;
import com.enigmacamp.loan_app.service.LoanTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanTypeServiceImpl implements LoanTypeService {

    private final LoanTypeRepository loanTypeRepository;
    
    @Override
    public LoanType create(LoanTypeRequest request) {
        LoanType loanType = LoanType.builder()
                .type(request.getType())
                .maxLoan(request.getMaxLoan())
                .build();
        
        return loanTypeRepository.saveAndFlush(loanType);
    }

    @Override
    public LoanType getById(String id) {
        return findByIdOrElseThrow(id);
    }

    @Override
    public List<LoanType> getAll() {
        return loanTypeRepository.findAll();
    }

    @Override
    public LoanType update(LoanType type) {
        findByIdOrElseThrow(type.getId());
        return loanTypeRepository.saveAndFlush(type);
    }

    @Override
    public void delete(String id) {
        LoanType type = findByIdOrElseThrow(id);
        loanTypeRepository.delete(type);
    }

    public LoanType findByIdOrElseThrow(String id) {
        return loanTypeRepository.findById(id).orElseThrow(() -> new RuntimeException(
                "Loan Type" +
                        " not " +
                        "found"));
    }
}
