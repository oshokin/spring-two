package com.geekbrains.springbootproject.services;

import com.geekbrains.springbootproject.entities.Product;

import java.util.Map;

public interface ProductsService {

    Product findByTitle(String title);
    Product findById(Long id);
    Map<String, Object> applyFilter(Map<String, Object> parameters);
    Product saveOrUpdate(Product product);

}
