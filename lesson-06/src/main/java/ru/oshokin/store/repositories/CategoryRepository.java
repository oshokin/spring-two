package ru.oshokin.store.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.oshokin.store.entities.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
}
