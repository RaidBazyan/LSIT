package com.list_project.lsit.s3_repositories;

import java.net.URI;
import java.util.*;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.list_project.lsit.Models.WarehouseManager;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Repository
public class WarehouseRepository_s3 {

    private static final String BUCKET = "lsit-project";
    private static final String PREFIX = "";
    private static final String ACCESS_KEY = ""; 
    private static final String SECRET_KEY = ""; 
    private final String ENDPOINT_URL = "";

    private final AmazonS3 s3Client;

    public WarehouseRepository_s3() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        s3Client = AmazonS3ClientBuilder.standard()
                .withRegion("eu-north-1")
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    // Add a new WarehouseManager
    public void add(WarehouseManager manager) {
        try {
            manager.setId(UUID.randomUUID().getMostSignificantBits()); // Set a new UUID for the manager
            ObjectMapper objectMapper = new ObjectMapper();
            String managerJson = objectMapper.writeValueAsString(manager);

            // Upload the manager to the S3 bucket
            s3Client.putObject(BUCKET, PREFIX + manager.getId(), managerJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // Get a WarehouseManager by ID
    public WarehouseManager get(Long managerId) {
        try {
            S3Object object = s3Client.getObject(BUCKET, PREFIX + managerId);
            byte[] objectBytes = object.getObjectContent().readAllBytes();

            // Deserialize the object into a WarehouseManager
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(objectBytes, WarehouseManager.class);
        } catch (Exception e) {
            return null; // If not found or any error occurs
        }
    }

    // Remove a WarehouseManager by ID
    public void remove(String id) {
        s3Client.deleteObject(BUCKET, PREFIX + id);
    }

    // Update a WarehouseManager
    public void update(WarehouseManager manager) {
        // First, get the existing WarehouseManager
        WarehouseManager existingManager = get(manager.getId());
        if (existingManager != null) {
            // Update the fields if necessary
            existingManager.setName(manager.getName());
            existingManager.setContact(manager.getContact());
            // Save the updated WarehouseManager back to S3
            add(existingManager);
        }
    }

    // List all WarehouseManagers
    public List<WarehouseManager> list() {
        List<WarehouseManager> managers = new ArrayList<>();
        List<S3ObjectSummary> objects = s3Client.listObjects(BUCKET).getObjectSummaries();

        // For each object in the bucket, retrieve and deserialize it
        for (S3ObjectSummary o : objects) {
            try {
                String managerId = o.getKey().substring(PREFIX.length());
                WarehouseManager manager = get(Long.valueOf(managerId));
                if (manager != null) {
                    managers.add(manager);
                }
            } catch (Exception e) {
                // Handle any errors, such as failed deserialization
            }
        }
        return managers;
    }
}
