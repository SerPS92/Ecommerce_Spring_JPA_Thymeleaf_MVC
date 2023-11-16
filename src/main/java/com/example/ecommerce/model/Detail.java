package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Detail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private int quantity;
    private double price;
    private double total;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;
}
