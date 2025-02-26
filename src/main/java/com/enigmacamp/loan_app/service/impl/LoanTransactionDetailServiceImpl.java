package com.enigmacamp.loan_app.service.impl;

import com.enigmacamp.loan_app.entity.LoanTransactionDetail;
import com.enigmacamp.loan_app.repository.LoanTransactionDetailRepository;
import com.enigmacamp.loan_app.service.LoanTransactionDetailService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanTransactionDetailServiceImpl implements LoanTransactionDetailService {
    private final LoanTransactionDetailRepository loanTransactionDetailRepository;

    @Override
    public void createBulk(List<LoanTransactionDetail> loanTransactionDetails) {
        loanTransactionDetailRepository.saveAllAndFlush(loanTransactionDetails);
    }

    @Override
    public void update(LoanTransactionDetail transactionDetail) {
        loanTransactionDetailRepository.saveAndFlush(transactionDetail);
    }
}
