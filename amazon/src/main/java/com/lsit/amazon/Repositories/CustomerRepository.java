package lsit.Repositories;

import java.util.*;

import org.springframework.stereotype.Repository;

import lsit.Models.Customer;

@Repository
public class CustomerRepository {
    private static final Map<Long, Customer> customers = new HashMap<>();
    private static long currentId = 1;

    public void add(Customer customer) {
        customer.setId(currentId++);
        customers.put(customer.getId(), customer);
    }

    public Customer get(Long id) {
        return customers.get(id);
    }

    public void remove(Long id) {
        customers.remove(id);
    }

    public List<Customer> list() {
        return new ArrayList<>(customers.values());
    }
}
