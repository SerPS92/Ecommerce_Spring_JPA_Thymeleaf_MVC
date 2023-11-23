package com.example.ecommerce.service;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    List<Order> findAll();
    Optional<Order> findById(Integer id);
    Order save(Order order);
    List<Order> findByUser(User user);
    Page<Order> getOrders(int page, int size);
    String getOrderNumber();
}
