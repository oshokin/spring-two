package ru.oshokin.store.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.oshokin.store.entities.Product;
import ru.oshokin.store.repositories.ProductRepository;
import ru.oshokin.store.repositories.specifications.ProductSpecs;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return (List<Product>)(productRepository.findAll());
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Product> applyFilter(Map<String, Object> parameters) {

        //через лямбду (функциональные интерфейсы Consumer, Function),
        //Java просит чтобы Specification<Product> spec был final,
        //красивее не сделать я думаю, или у меня не хватает опыта.
        //
        //PS: это все очень похоже на объект QueryBuilder в 1С,
        //он строит SQL запрос со списком заготавливаемых условий.

        List<String> filterFields = Arrays.asList("word", "min", "max");
        Specification<Product> spec = Specification.where(null);
        StringBuilder filters = new StringBuilder();

        for (String fieldName : filterFields) {
            Object fieldValue = parameters.get(fieldName);
            if (fieldValue == null) continue;
            if (fieldName.equals("word")) {
                String castedValue = (String) fieldValue;
                if (!castedValue.isEmpty()) spec = spec.and(ProductSpecs.titleContains(castedValue));
                filters.append("&word=" + castedValue);
            }
            if (fieldName.equals("min")) {
                Double castedValue = (Double) fieldValue;
                spec = spec.and(ProductSpecs.priceGreaterThanOrEq(castedValue));
                filters.append("&min=" + castedValue);
            }
            if (fieldName.equals("max")) {
                Double castedValue = (Double) fieldValue;
                spec = spec.and(ProductSpecs.priceLesserThanOrEq(castedValue));
                filters.append("&max=" + castedValue);
            }
        }

        int page = (int) parameters.get("page");
        int size = (int) parameters.get("size");

        return productRepository.findAll(spec, PageRequest.of(page - 1, size));
    }

    @Override
    public boolean isProductWithTitleExists(String productTitle) {
        return productRepository.findOneByTitle(productTitle) != null;
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }
}
