package com.list_project.lsit.cloud_repositories;

import java.util.*;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.list_project.lsit.Models.Customer;
import com.list_project.lsit.Services.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class CustomerRepository_s3 {
    private final StorageService storageService;
    private final ObjectMapper objectMapper;
    private final String prefix;
    private static final Logger log = LoggerFactory.getLogger(CustomerRepository_s3.class);

    public CustomerRepository_s3(StorageService storageService, 
                               @Value("${gcp.storage.prefix:customers/}") String prefix) {
        this.storageService = storageService;
        this.objectMapper = new ObjectMapper();
        this.prefix = prefix;
    }

    public void add(Customer customer) {
        try {
            String key = prefix + customer.getId();
            String customerJson = objectMapper.writeValueAsString(customer);
            storageService.uploadFile(key, customerJson.getBytes());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize customer", e);
        }
    }

    public Customer get(Long id) {
        try {
            String key = prefix + id;
            byte[] content = storageService.downloadFile(key);
            return objectMapper.readValue(content, Customer.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void remove(Long id) {
        String key = prefix + id;
        storageService.deleteFile(key);
    }

    public List<Customer> list() {
        List<Customer> customers = new ArrayList<>();
        List<String> files = storageService.listFiles(prefix);
        
        for (String file : files) {
            try {
                String id = file.substring(prefix.length());
                Customer customer = get(Long.valueOf(id));
                if (customer != null) {
                    customers.add(customer);
                }
            } catch (Exception e) {
                log.error("Error loading customer from file {}: {}", file, e.getMessage());
            }
        }
        return customers;
    }
}
