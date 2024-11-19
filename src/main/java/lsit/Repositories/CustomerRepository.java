
package lsit.Repositories;

import lsit.Models.Customer;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CustomerRepository {
    private final Map<Long, Customer> customers = new HashMap<>();

    public Customer save(Customer customer) {
        customers.put(customer.getId(), customer);
        return customer;
    }

    public Customer findById(Long id) {
        return customers.get(id);
    }
}
