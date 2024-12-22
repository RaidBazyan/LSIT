package com.list_project.lsit.cloud_repositories;

import java.util.*;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.list_project.lsit.Models.DeliveryDriver;
import com.list_project.lsit.Services.StorageService;
import org.springframework.beans.factory.annotation.Value;

@Repository
public class DeliveryRepository_s3 {
    private final StorageService storageService;
    private final ObjectMapper objectMapper;
    private final String prefix;

    public DeliveryRepository_s3(StorageService storageService,
                                @Value("${gcp.storage.prefix:drivers/}") String prefix) {
        this.storageService = storageService;
        this.objectMapper = new ObjectMapper();
        this.prefix = prefix;
    }

    public void add(DeliveryDriver driver) {
        try {
            driver.setId(UUID.randomUUID().getMostSignificantBits());
            String key = prefix + driver.getId();
            String driverJson = objectMapper.writeValueAsString(driver);
            storageService.uploadFile(key, driverJson.getBytes());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize driver", e);
        }
    }

    public DeliveryDriver get(String id) {
        try {
            String key = prefix + id;
            byte[] content = storageService.downloadFile(key);
            return objectMapper.readValue(content, DeliveryDriver.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void remove(String id) {
        String key = prefix + id;
        storageService.deleteFile(key);
    }

    public List<DeliveryDriver> list() {
        List<DeliveryDriver> drivers = new ArrayList<>();
        // Implementation for listing objects will be added
        return drivers;
    }
}
