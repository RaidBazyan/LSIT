package com.list_project.lsit.s3_repositories;

import java.net.URI;
import java.util.*;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.list_project.lsit.Models.Customer;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Repository
public class CustomerRepository_s3 {

    private static final String BUCKET = "lsit-project";
    private static final String PREFIX = "customers/";
    private static final String ACCESS_KEY = "AKIATG6MGQSAYOUKHH6Q"; 
    private static final String SECRET_KEY = "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"; 
    private final String ENDPOINT_URL = "https://s3.amazonaws.com";

    private S3Client s3client;

    public CustomerRepository_s3() {
      AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
      s3client = S3Client.builder()
              .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
              .endpointOverride(URI.create(ENDPOINT_URL))
              .region(Region.EU_NORTH_1)
              .build();
  }

    // Add a new Customer to S3
    public void add(Customer customer) {
        try {
            // Use a unique identifier (e.g., customer ID or UUID) for S3 object key
            String customerKey = PREFIX + customer.getId();
            ObjectMapper om = new ObjectMapper();
            String customerJson = om.writeValueAsString(customer);

            s3client.putObject(PutObjectRequest.builder()
                            .bucket(BUCKET)
                            .key(customerKey)
                            .build(),
                    RequestBody.fromString(customerJson)
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // Get a Customer by ID from S3
    public Customer get(Long id) {
        try {
            String customerKey = PREFIX + id;
            var objectBytes = s3client.getObject(GetObjectRequest.builder()
                    .bucket(BUCKET)
                    .key(customerKey)
                    .build()
            ).readAllBytes();

            ObjectMapper om = new ObjectMapper();
            return om.readValue(objectBytes, Customer.class);
        } catch (Exception e) {
            return null; // Customer not found or error occurred
        }
    }

    // Remove a Customer from S3 by ID
    public void remove(Long id) {
        try {
            String customerKey = PREFIX + id;
            s3client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(BUCKET)
                    .key(customerKey)
                    .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // List all customers from S3
    public List<Customer> list() {
        List<Customer> customersList = new ArrayList<>();
        try {
            List<S3Object> objects = s3client.listObjects(ListObjectsRequest.builder()
                    .bucket(BUCKET)
                    .prefix(PREFIX)
                    .build()
            ).contents();

            for (S3Object o : objects) {
                try {
                    // Extract the customer ID from the S3 key and get the customer object
                    Long customerId = Long.parseLong(o.key().substring(PREFIX.length()));
                    Customer customer = get(customerId);
                    if (customer != null) {
                        customersList.add(customer);
                    }
                } catch (Exception e) {
                    // Handle parsing issues or missing customers
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customersList;
    }
}
