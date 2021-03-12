package ru.oshokin.store.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.oshokin.store.entities.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
}
