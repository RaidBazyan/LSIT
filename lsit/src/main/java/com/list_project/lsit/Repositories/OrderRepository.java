package com.list_project.lsit.Repositories;

import java.util.*;
import org.springframework.stereotype.Repository;
import com.list_project.lsit.Models.Order;

@Repository
public class OrderRepository {
    private static final Map<Long, Order> orders = new HashMap<>();
    private static long currentId = 1;

    public void add(Order order) {
        order.setId(currentId++);
        orders.put(order.getId(), order);
    }

    public Order get(Long id) {
        return orders.get(id);
    }

    public List<Order> getCustomerOrders(Long customerId) {
        return orders.values().stream()
                .filter(order -> order.getCustomerId().equals(customerId))
                .toList();
    }

    public void updateStatus(Long id, Order.OrderStatus status) {
        Order order = orders.get(id);
        if (order != null) {
            order.setStatus(status);
        }
    }

    public void remove(Long id) {
        orders.remove(id);
    }

    public List<Order> list() {
        return new ArrayList<>(orders.values());
    }
}