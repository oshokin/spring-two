package ru.oshokin.store.services;

import org.springframework.data.domain.Page;
import ru.oshokin.store.entities.Product;

import java.util.Map;

public interface ProductsService {

    Product findByTitle(String title);
    Product findById(Long id);
    Page<Product> applyFilter(Map<String, Object> parameters);
    Product saveOrUpdate(Product product);

}
