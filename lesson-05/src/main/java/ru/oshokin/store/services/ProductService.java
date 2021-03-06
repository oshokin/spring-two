package ru.oshokin.store.services;

import org.springframework.data.domain.Page;
import ru.oshokin.store.entities.Product;

import java.util.Map;

public interface ProductService {

    Product getProductById(Long id);
    Page<Product> applyFilter(Map<String, Object> parameters);
    boolean isProductWithTitleExists(String productTitle);
    void saveProduct(Product product);

}
