package com.example.ecommerce.repository;

import com.example.ecommerce.model.Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDetailsRepo extends JpaRepository<Detail, Integer> {

}
