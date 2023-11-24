package com.example.ecommerce.repository;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepo extends JpaRepository<Order, Integer> {
    Page<Order> findByUser(User user, Pageable pageable);
}
