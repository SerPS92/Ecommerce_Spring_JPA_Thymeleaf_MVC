package com.example.ecommerce.service;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    Optional<Order> findById(Integer id);
    Order save(Order order);
    Page<Order> findByUser(User user, Pageable pageable);
    Page<Order> getOrders(Pageable pageable);
    String getOrderNumber();
}
