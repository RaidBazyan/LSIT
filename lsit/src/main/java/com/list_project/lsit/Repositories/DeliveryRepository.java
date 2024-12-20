package com.list_project.lsit.Repositories;

import java.util.*;

import org.springframework.stereotype.Repository;

import com.list_project.lsit.Models.DeliveryDriver;

@Repository
public class DeliveryRepository {
    private static final Map<Long, DeliveryDriver> deliveryDrivers = new HashMap<>();
    private static long currentId = 1;

    public void add(DeliveryDriver driver) {
        driver.setId(currentId++);
        deliveryDrivers.put(driver.getId(), driver);
    }

    public DeliveryDriver get(Long id) {
        return deliveryDrivers.get(id);
    }

    public void remove(Long id) {
        deliveryDrivers.remove(id);
    }

    public List<DeliveryDriver> list() {
        return new ArrayList<>(deliveryDrivers.values());
    }
}
