package com.geekbrains.springbootproject.services;

import com.geekbrains.springbootproject.entities.Product;
import com.geekbrains.springbootproject.repositories.ProductsRepository;
import com.geekbrains.springbootproject.repositories.specifications.ProductSpecs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductsServiceImpl implements ProductsService {

    @AllArgsConstructor
    private class FilteringResults {
        @Getter
        @Setter
        @NonNull
        private Page<Product> page;

        @Getter
        @Setter
        @NonNull
        private String presentation;

        @Getter
        @Setter
        @NonNull
        private List<String> fields;
    }

    private ProductsRepository productsRepository;

    @Autowired
    public void setProductsRepository(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Override
    public Product findByTitle(String title) {
        return productsRepository.findOneByTitle(title);
    }

    @Override
    public Product findById(Long id) {
        return productsRepository.findById(id).orElse(null);
    }

    public List<Product> getAllProducts() {
        return (List<Product>) productsRepository.findAll();
    }

    public Page<Product> getProductsByCost(Pageable pageable, Double min, Double max) {
        if (min == null) {
            min = 0.0;
        }
        if (max == null) {
            max = Double.MAX_VALUE;
        }
        return productsRepository.findAllByPriceBetween(pageable, min, max);
    }

    @Override
    public Map<String, Object> applyFilter(Map<String, Object> parameters) {
        FilteringResults fr = getFilteringResults(parameters);
        Page<Product> products = fr.getPage();

        Map<String, Object> funcResult = new HashMap<>(4);

        funcResult.put("products", products.getContent());
        funcResult.put("totalPage", products.getTotalPages());
        funcResult.put("filters", fr.getPresentation());
        funcResult.put("page", parameters.get("page"));

        fr.getFields().forEach(fieldName -> funcResult.put(fieldName, parameters.get(fieldName)));

        return funcResult;

    }

    @Override
    public Product saveOrUpdate(Product product) {
        return productsRepository.save(product);
    }

    private FilteringResults getFilteringResults(Map<String, Object> parameters) {

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

        return new FilteringResults(productsRepository.findAll(spec, PageRequest.of(page - 1, size)), filters.toString(), filterFields);

    }

}
