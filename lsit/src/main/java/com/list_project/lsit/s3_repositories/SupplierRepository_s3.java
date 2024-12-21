package com.list_project.lsit.s3_repositories;

import java.net.URI;
import java.util.*;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.list_project.lsit.Models.Supplier;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Repository
public class SupplierRepository_s3 {

    private static final String BUCKET = "lsit-project";
    private static final String PREFIX = "suppliers/";
    private static final String ACCESS_KEY = "AKIATG6MGQSAYOUKHH6Q"; 
    private static final String SECRET_KEY = "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"; 
    private final String ENDPOINT_URL = "https://s3.amazonaws.com";

    private S3Client s3client;

    public SupplierRepository_s3() {
      AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
      s3client = S3Client.builder()
              .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
              .endpointOverride(URI.create(ENDPOINT_URL))
              .region(Region.EU_NORTH_1)
              .build();
  }

    // Add a new Supplier
    public void add(Supplier supplier) {
        try {
            supplier.setId(UUID.randomUUID().getMostSignificantBits());
            ObjectMapper objectMapper = new ObjectMapper();
            String supplierJson = objectMapper.writeValueAsString(supplier);

            s3client.putObject(PutObjectRequest.builder()
                    .bucket(BUCKET)
                    .key(PREFIX + supplier.getId()) // Use the supplier ID as the key
                    .build(),
                    RequestBody.fromString(supplierJson)
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // Get a Supplier by ID
    public Supplier get(Long id) {
        try {
            // Retrieve the object from S3
            var objectBytes = s3client.getObject(GetObjectRequest.builder()
                    .bucket(BUCKET)
                    .key(PREFIX + id) // Use the supplier ID as the key
                    .build()
            ).readAllBytes();

            // Deserialize the object into a Supplier
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(objectBytes, Supplier.class);
        } catch (Exception e) {
            return null; // If not found or any error occurs
        }
    }

    // Remove a Supplier by ID
    public void remove(Long id) {
        s3client.deleteObject(DeleteObjectRequest.builder()
                .bucket(BUCKET)
                .key(PREFIX + id) // Use the supplier ID as the key
                .build()
        );
    }

    // Update a Supplier
    public void update(Supplier supplier) {
        // First, get the existing supplier
        Supplier existingSupplier = get(supplier.getId());
        if (existingSupplier != null) {
            // Update the fields if necessary
            existingSupplier.setName(supplier.getName());
            existingSupplier.setContact(supplier.getContact());
            // Save the updated supplier back to S3
            add(existingSupplier);
        }
    }

    // List all Suppliers
    public List<Supplier> list() {
        List<Supplier> suppliers = new ArrayList<>();
        List<S3Object> objects = s3client.listObjects(ListObjectsRequest.builder()
                .bucket(BUCKET)
                .prefix(PREFIX) // Optional: List objects with the specific prefix (directory)
                .build()
        ).contents();

        // For each object in the bucket, retrieve and deserialize it
        for (S3Object o : objects) {
            try {
                String supplierId = o.key().substring(PREFIX.length());
                Supplier supplier = get(Long.valueOf(supplierId));
                if (supplier != null) {
                    suppliers.add(supplier);
                }
            } catch (Exception e) {
                // Handle any errors, such as failed deserialization
            }
        }
        return suppliers;
    }
}
