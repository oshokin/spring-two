package ru.oshokin.store.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.oshokin.store.entities.OrderStatus;
import ru.oshokin.store.repositories.OrderStatusRepository;

@Service
public class OrderStatusService {
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    public void setOrderStatusRepository(OrderStatusRepository orderStatusRepository) {
        this.orderStatusRepository = orderStatusRepository;
    }

    public OrderStatus getStatusById(Long id) {
        return orderStatusRepository.findById(id).orElse(null);
    }
}
