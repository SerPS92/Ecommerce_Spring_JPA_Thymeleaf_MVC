package com.example.ecommerce.service;

import com.example.ecommerce.model.Category;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {

    Category save(Category category);
    List<Category> findAll();
    Optional<Category> findByName(String name);
    Page<Category> getCategories(int page, int size);
    Optional<Category> findById(Integer id);
    void delete(Integer id);
    void update(Category category);

}
