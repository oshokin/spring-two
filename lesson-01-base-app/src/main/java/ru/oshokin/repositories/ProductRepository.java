package ru.oshokin.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.oshokin.entities.Product;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    Page<Product> findAllByPriceBetween(Pageable pageable, double min, double max);
    Product findOneByTitle(String title);

}
