package com.example.ecommerce.service;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.IOrderRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OrderServiceImpl implements IOrderService {

    private final IOrderRepo orderRepo;

    public OrderServiceImpl(IOrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }


    @Override
    public Optional<Order> findById(Integer id) {
        return orderRepo.findById(id);
    }

    @Override
    public Order save(Order order) {
        return orderRepo.save(order);
    }

    @Override
    public Page<Order> findByUser(User user, Pageable pageable) {
        return orderRepo.findByUser(user, pageable);
    }

    @Override
    public Page<Order> getOrders(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("id").descending());
        return orderRepo.findAll(sortedPageable);
    }

    @Override
    public String getOrderNumber() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = dateFormat.format(date);
        Random random = new Random();
        int randomNumber = random.nextInt(10000);

        return formattedDate + randomNumber;
    }
}
