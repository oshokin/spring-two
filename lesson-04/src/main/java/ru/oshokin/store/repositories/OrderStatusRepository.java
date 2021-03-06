package ru.oshokin.store.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.oshokin.store.entities.OrderStatus;

@Repository
public interface OrderStatusRepository extends CrudRepository<OrderStatus, Long> {
}
