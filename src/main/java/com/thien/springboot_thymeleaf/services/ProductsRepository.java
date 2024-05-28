package com.thien.springboot_thymeleaf.services;

import com.thien.springboot_thymeleaf.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Product, Integer> {
}
