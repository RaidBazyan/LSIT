package com.list_project.lsit.s3_repositories;

import java.net.URI;
import java.util.*;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.list_project.lsit.Models.DeliveryDriver;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Repository
public class DeliveryRepository_s3 {

    private static final String BUCKET = "lsit-project";
    private static final String PREFIX = "delivery/";
    private static final String ACCESS_KEY = ""; 
    private static final String SECRET_KEY = ""; 
    private final String ENDPOINT_URL = "https://s3.amazonaws.com";

    private S3Client s3client;

    public DeliveryRepository_s3() {
      AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
      s3client = S3Client.builder()
              .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
              .endpointOverride(URI.create(ENDPOINT_URL))
              .region(Region.EU_NORTH_1)
              .build();
  }

    // Add a new DeliveryDriver
    public void add(DeliveryDriver driver) {
        try {
            driver.setId(UUID.randomUUID().getMostSignificantBits()); // Set a new UUID for the driver
            ObjectMapper objectMapper = new ObjectMapper();
            String driverJson = objectMapper.writeValueAsString(driver);

            s3client.putObject(PutObjectRequest.builder()
                    .bucket(BUCKET)
                    .key(PREFIX + driver.getId()) // Use the driver ID as the key
                    .build(),
                    RequestBody.fromString(driverJson)
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // Get a DeliveryDriver by ID
    public DeliveryDriver get(String id) {
        try {
            // Retrieve the object from S3
            var objectBytes = s3client.getObject(GetObjectRequest.builder()
                    .bucket(BUCKET)
                    .key(PREFIX + id) // Use the driver ID as the key
                    .build()
            ).readAllBytes();

            // Deserialize the object into a DeliveryDriver
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(objectBytes, DeliveryDriver.class);
        } catch (Exception e) {
            return null; // If not found or any error occurs
        }
    }

    // Remove a DeliveryDriver by ID
    public void remove(String id) {
        s3client.deleteObject(DeleteObjectRequest.builder()
                .bucket(BUCKET)
                .key(PREFIX + id) // Use the driver ID as the key
                .build()
        );
    }

    // List all DeliveryDrivers
    public List<DeliveryDriver> list() {
        List<DeliveryDriver> drivers = new ArrayList<>();
        List<S3Object> objects = s3client.listObjects(ListObjectsRequest.builder()
                .bucket(BUCKET)
                .prefix(PREFIX) // Optional: List objects with the specific prefix (directory)
                .build()
        ).contents();

        // For each object in the bucket, retrieve and deserialize it
        for (S3Object o : objects) {
            try {
                String driverId = o.key().substring(PREFIX.length());
                DeliveryDriver driver = get(driverId);
                if (driver != null) {
                    drivers.add(driver);
                }
            } catch (Exception e) {
                // Handle any errors, such as failed deserialization
            }
        }
        return drivers;
    }
}
