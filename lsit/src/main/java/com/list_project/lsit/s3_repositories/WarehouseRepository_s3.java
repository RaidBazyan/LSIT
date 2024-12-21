package com.list_project.lsit.s3_repositories;

import java.net.URI;
import java.util.*;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.list_project.lsit.Models.WarehouseManager;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Repository
public class WarehouseRepository_s3 {

    private static final String BUCKET = "lsit-project";
    private static final String PREFIX = "warehouse/";
    private static final String ACCESS_KEY = "AKIATG6MGQSAYOUKHH6Q"; 
    private static final String SECRET_KEY = "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"; 
    private final String ENDPOINT_URL = "https://s3.amazonaws.com";

    private S3Client s3client;

    public WarehouseRepository_s3() {
      AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
      s3client = S3Client.builder()
              .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
              .endpointOverride(URI.create(ENDPOINT_URL))
              .region(Region.EU_NORTH_1)
              .build();
  }

    // Add a new WarehouseManager
    public void add(WarehouseManager manager) {
        try {
            manager.setId(UUID.randomUUID().getMostSignificantBits()); // Set a new UUID for the manager
            ObjectMapper objectMapper = new ObjectMapper();
            String managerJson = objectMapper.writeValueAsString(manager);

            // Upload the manager to the S3 bucket
            s3client.putObject(PutObjectRequest.builder()
                    .bucket(BUCKET)
                    .key(PREFIX + manager.getId()) // Use the manager ID as the key
                    .build(),
                    RequestBody.fromString(managerJson)
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // Get a WarehouseManager by ID
    public WarehouseManager get(Long managerId) {
                try {
                    // Retrieve the object from S3
                    var objectBytes = s3client.getObject(GetObjectRequest.builder()
                            .bucket(BUCKET)
                            .key(PREFIX + managerId) // Use the manager ID as the key
                    .build()
            ).readAllBytes();

            // Deserialize the object into a WarehouseManager
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(objectBytes, WarehouseManager.class);
        } catch (Exception e) {
            return null; // If not found or any error occurs
        }
    }

    // Remove a WarehouseManager by ID
    public void remove(String id) {
        s3client.deleteObject(DeleteObjectRequest.builder()
                .bucket(BUCKET)
                .key(PREFIX + id) // Use the manager ID as the key
                .build()
        );
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
        List<S3Object> objects = s3client.listObjects(ListObjectsRequest.builder()
                .bucket(BUCKET)
                .prefix(PREFIX) // Optional: List objects with the specific prefix (directory)
                .build()
        ).contents();

        // For each object in the bucket, retrieve and deserialize it
        for (S3Object o : objects) {
            try {
                String managerId = o.key().substring(PREFIX.length());
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
