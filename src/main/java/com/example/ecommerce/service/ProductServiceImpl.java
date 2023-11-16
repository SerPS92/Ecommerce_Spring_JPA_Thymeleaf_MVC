package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.IProductRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService{

    private final IProductRepo productRepo;

    public ProductServiceImpl(IProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public List<Product> findAll() {
        return productRepo.findAll();
    }

    @Override
    public Product save(Product product) {
        return productRepo.save(product);
    }

    @Override
    public Optional<Product> findById(Integer id) {
        return productRepo.findById(id);
    }

    @Override
    public void update(Product product) {
        productRepo.save(product);
    }

    @Override
    public void delete(Integer id) {
        productRepo.deleteById(id);
    }

    @Override
    public Page<Product> getProducts(Pageable pageable) {
        return  productRepo.findAll(pageable);
    }

    @Override
    public List<Product> findByOffer() {
        return productRepo.findByOffer("Yes");
    }

    @Override
    public Page<Product> getProductsByCategory(Integer categoryId, Pageable pageable) {
        return productRepo.findByCategoryId(categoryId, pageable);
    }

    @Override
    public Page<Product> getProductsByPrice(Double minPrice, Double maxPrice, Pageable pageable) {
        return productRepo.findByPriceBetween(minPrice, maxPrice, pageable);
    }

    @Override
    public Page<Product> getProductsByCategoryAndPrice(Double minPrice, Double maxPrice, Integer categoryId,
                                                       Pageable pageable) {
        return productRepo.findByCategoryAndPriceBetween(minPrice, maxPrice, categoryId, pageable);
    }

}
