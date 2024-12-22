package com.list_project.lsit.s3_repositories;

import java.util.*;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.list_project.lsit.Models.Supplier;
import com.list_project.lsit.Services.StorageService;
import org.springframework.beans.factory.annotation.Value;

@Repository
public class SupplierRepository_s3 {
    private final StorageService storageService;
    private final ObjectMapper objectMapper;
    private final String prefix;

    public SupplierRepository_s3(StorageService storageService,
                                @Value("${gcp.storage.prefix:suppliers/}") String prefix) {
        this.storageService = storageService;
        this.objectMapper = new ObjectMapper();
        this.prefix = prefix;
    }

    public void add(Supplier supplier) {
        try {
            supplier.setId(UUID.randomUUID().getMostSignificantBits());
            String key = prefix + supplier.getId();
            String supplierJson = objectMapper.writeValueAsString(supplier);
            storageService.uploadFile(key, supplierJson.getBytes());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize supplier", e);
        }
    }

    public Supplier get(Long id) {
        try {
            String key = prefix + id;
            byte[] content = storageService.downloadFile(key);
            return objectMapper.readValue(content, Supplier.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void remove(Long id) {
        String key = prefix + id;
        storageService.deleteFile(key);
    }

    public void update(Supplier supplier) {
        try {
            String key = prefix + supplier.getId();
            String supplierJson = objectMapper.writeValueAsString(supplier);
            storageService.uploadFile(key, supplierJson.getBytes());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize supplier", e);
        }
    }

    public List<Supplier> list() {
        List<Supplier> suppliers = new ArrayList<>();
        // Implementation for listing objects will be added
        return suppliers;
    }
}
