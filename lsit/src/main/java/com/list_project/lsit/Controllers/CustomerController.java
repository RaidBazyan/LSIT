package com.list_project.lsit.Controllers;

import com.list_project.lsit.Models.Customer;
import com.list_project.lsit.Repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Create
    @PostMapping("")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        customerRepository.add(customer);
        return ResponseEntity.ok(customer);
    }

    // Read
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        Customer customer = customerRepository.get(id);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
    }

    @GetMapping("/listall")
    public ResponseEntity<?> listAllCustomers() {
        List<Customer> customers = customerRepository.list();
        if (customers.isEmpty()) {
            return ResponseEntity.ok(Collections.singletonMap("message", "No customers currently stored."));
        }
        return ResponseEntity.ok(customers);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {
        Customer existingCustomer = customerRepository.get(id);
        if (existingCustomer == null) {
            return ResponseEntity.notFound().build();
        }
        updatedCustomer.setId(id);
        customerRepository.add(updatedCustomer);
        return ResponseEntity.ok(updatedCustomer);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        Customer existingCustomer = customerRepository.get(id);
        if (existingCustomer == null) {
            return ResponseEntity.notFound().build();
        }
        customerRepository.remove(id);
        return ResponseEntity.noContent().build();
    }
}
