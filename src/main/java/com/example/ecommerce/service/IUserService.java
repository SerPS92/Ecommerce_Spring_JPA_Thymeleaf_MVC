package com.example.ecommerce.service;

import com.example.ecommerce.model.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    Optional<User> findById(Integer id);
    User save(User user);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    void deleteById(Integer id);
    void update(User user);
    Page<User> getUsers(int page, int size);

}
