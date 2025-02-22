package com.enigmacamp.loan_app.service.impl;

import com.enigmacamp.loan_app.entity.LoanType;
import com.enigmacamp.loan_app.model.request.LoanTypeRequest;
import com.enigmacamp.loan_app.model.response.LoanResponse;
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
    public LoanResponse create(LoanTypeRequest request) {
        LoanType loanType = LoanType.builder()
                .type(request.getType())
                .maxLoan(request.getMaxLoan())
                .build();


        loanTypeRepository.saveAndFlush(loanType);

        return toLoanResponse(loanType);
    }

    @Override
    public LoanResponse getById(String id) {
        return toLoanResponse(findByIdOrElseThrow(id));
    }

    @Override
    public List<LoanResponse> getAll() {
        return loanTypeRepository.findAll().stream()
                .map(LoanTypeServiceImpl::toLoanResponse)
                .toList();
    }

    @Override
    public LoanResponse update(LoanType type) {
        findByIdOrElseThrow(type.getId());
        return toLoanResponse(loanTypeRepository.saveAndFlush(type));
    }

    @Override
    public void delete(String id) {
        LoanType type = findByIdOrElseThrow(id);
        loanTypeRepository.delete(type);
    }

    public LoanType findByIdOrElseThrow(String id) {
        return loanTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("Loan Type" + " not " + "found"));
    }

    private static LoanResponse toLoanResponse(LoanType loanType) {
        return LoanResponse.builder()
                .id(loanType.getId())
                .type(loanType.getType())
                .maxLoan(loanType.getMaxLoan().longValue())
                .build();
    }
}
