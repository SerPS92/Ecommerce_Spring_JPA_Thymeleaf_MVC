package com.example.ecommerce.service;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.repository.ICategoryRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryRepo categoryRepo;

    public CategoryServiceImpl(ICategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public Category save(Category category) {
        return categoryRepo.save(category);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepo.findAll();
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryRepo.findByName(name);
    }

    @Override
    public Page<Category> getCategories(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return categoryRepo.findAll(pageRequest);
    }
    @Override
    public Optional<Category> findById(Integer id) {
        return categoryRepo.findById(id);
    }

    @Override
    public void delete(Integer id) {
        categoryRepo.deleteById(id);
    }

    @Override
    public void update(Category category) {
        categoryRepo.save(category);
    }


}
