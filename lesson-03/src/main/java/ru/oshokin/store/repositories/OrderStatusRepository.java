package ru.oshokin.store.repositories;

import ru.oshokin.store.entities.OrderStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends CrudRepository<OrderStatus, Long> {
}
