package com.list_project.lsit.s3_repositories;

import java.util.*;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.list_project.lsit.Models.WarehouseManager;
import com.list_project.lsit.Services.StorageService;
import org.springframework.beans.factory.annotation.Value;

@Repository
public class WarehouseRepository_s3 {
    private final StorageService storageService;
    private final ObjectMapper objectMapper;
    private final String prefix;

    public WarehouseRepository_s3(StorageService storageService,
                                 @Value("${gcp.storage.prefix:warehouses/}") String prefix) {
        this.storageService = storageService;
        this.objectMapper = new ObjectMapper();
        this.prefix = prefix;
    }

    public void add(WarehouseManager manager) {
        try {
            manager.setId(UUID.randomUUID().getMostSignificantBits());
            String key = prefix + manager.getId();
            String managerJson = objectMapper.writeValueAsString(manager);
            storageService.uploadFile(key, managerJson.getBytes());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize manager", e);
        }
    }

    public WarehouseManager get(Long managerId) {
        try {
            String key = prefix + managerId;
            byte[] content = storageService.downloadFile(key);
            return objectMapper.readValue(content, WarehouseManager.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void remove(String id) {
        String key = prefix + id;
        storageService.deleteFile(key);
    }

    public void update(WarehouseManager manager) {
        try {
            String key = prefix + manager.getId();
            String managerJson = objectMapper.writeValueAsString(manager);
            storageService.uploadFile(key, managerJson.getBytes());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize manager", e);
        }
    }

    public List<WarehouseManager> list() {
        List<WarehouseManager> managers = new ArrayList<>();
        // Implementation for listing objects will be added
        return managers;
    }
}
