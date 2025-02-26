package com.enigmacamp.loan_app.service.impl;

import com.enigmacamp.loan_app.constant.EInstalmentType;
import com.enigmacamp.loan_app.entity.InstalmentType;
import com.enigmacamp.loan_app.model.request.InstalmentTypeRequest;
import com.enigmacamp.loan_app.repository.InstalmentTypeRepository;
import com.enigmacamp.loan_app.service.InstalmentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstalmentTypeServiceImpl implements InstalmentTypeService {
    private final InstalmentTypeRepository instalmentTypeRepository;

    @Override
    public InstalmentType create(InstalmentTypeRequest request) {
        InstalmentType instalmentType = InstalmentType.builder()
                .instalmentType(EInstalmentType.valueOf(request.getInstalmentType()))
                .build();

        return instalmentTypeRepository.saveAndFlush(instalmentType);
    }

    @Override
    public InstalmentType getById(String id) {
        return findByIdOrElseThrow(id);
    }

    @Override
    public List<InstalmentType> getAll() {
        return instalmentTypeRepository.findAll();
    }

    @Override
    public InstalmentType update(InstalmentType type) {
        findByIdOrElseThrow(type.getId());
        return instalmentTypeRepository.saveAndFlush(type);
    }

    @Override
    public void delete(String id) {
        InstalmentType type = findByIdOrElseThrow(id);
        instalmentTypeRepository.delete(type);
    }

    public InstalmentType findByIdOrElseThrow(String id) {
        return instalmentTypeRepository.findById(id).orElseThrow(() -> new RuntimeException(
                "Instalment Type" +
                " not " +
                "found"));
    }
}
