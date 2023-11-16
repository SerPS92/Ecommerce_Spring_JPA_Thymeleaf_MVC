package com.example.ecommerce.service;

import com.example.ecommerce.model.Detail;
import com.example.ecommerce.repository.IDetailsRepo;
import org.springframework.stereotype.Service;

@Service
public class DetailsServiceImpl implements IDetailsService{

    private final IDetailsRepo detailsRepo;

    public DetailsServiceImpl(IDetailsRepo detailsRepo) {
        this.detailsRepo = detailsRepo;
    }

    @Override
    public Detail save(Detail detail) {
        return detailsRepo.save(detail);
    }
}
