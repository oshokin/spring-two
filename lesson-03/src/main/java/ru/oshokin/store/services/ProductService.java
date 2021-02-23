package ru.oshokin.store.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import ru.oshokin.store.entities.Product;
import ru.oshokin.store.repositories.ProductRepository;

import java.util.List;
import java.util.Map;

public interface ProductService {

    Product getProductById(Long id);
    Page<Product> applyFilter(Map<String, Object> parameters);
    boolean isProductWithTitleExists(String productTitle);
    void saveProduct(Product product);

}
