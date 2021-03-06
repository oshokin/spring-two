package ru.oshokin.store.services;

import ru.oshokin.store.entities.DeliveryAddress;
import ru.oshokin.store.repositories.DeliveryAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryAddressService {
    private DeliveryAddressRepository deliveryAddressRepository;

    @Autowired
    public void setDeliveryAddressRepository(DeliveryAddressRepository deliveryAddressRepository) {
        this.deliveryAddressRepository = deliveryAddressRepository;
    }

    public List<DeliveryAddress> getUserAddresses(Long userId) {
        return deliveryAddressRepository.findAllByUserId(userId);
    }

    public DeliveryAddress getUserAddressById(Long id) {
        return deliveryAddressRepository.findById(id).orElse(null);
    }
}
