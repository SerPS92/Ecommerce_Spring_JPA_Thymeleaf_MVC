package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    List<Product> findAll();
    Product save(Product product);
    Optional<Product> findById(Integer id);
    void update(Product product);
    void delete(Integer id);
    Page<Product> getProducts(Pageable pageable);
    List<Product> findByOffer();
    Page<Product> getProductsByCategory(Integer categoryId, Pageable pageable);
    Page<Product> getProductsByPrice(Double minPrice, Double maxPrice, Pageable pageable);
    Page<Product> getProductsByCategoryAndPrice(Double minPrice, Double maxPrice, Integer categoryId,
                                                Pageable pageable);

}
