package com.example.ecommerce.service;

import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.IUserRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService{

    private final IUserRepo userRepo;

    public UserServiceImpl(IUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepo.findById(id);
    }

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        userRepo.deleteById(id);
    }

    @Override
    public void update(User user) {
        userRepo.save(user);
    }

    @Override
    public Page<User> getUsers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return userRepo.findAll(pageRequest);
    }
}
