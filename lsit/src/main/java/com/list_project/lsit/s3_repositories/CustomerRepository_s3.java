package com.list_project.lsit.s3_repositories;

import java.util.*;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.list_project.lsit.Models.Customer;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Repository
public class CustomerRepository_s3 {
    private static final String BUCKET = "lsit-project";
    private static final String PREFIX = "";
    private static final String ACCESS_KEY = "";
    private static final String SECRET_KEY = "";
    private final AmazonS3 s3Client;

    public CustomerRepository_s3() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        s3Client = AmazonS3ClientBuilder.standard()
                .withRegion("eu-north-1")
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    public void add(Customer customer) {
        try {
            String customerKey = PREFIX + customer.getId();
            ObjectMapper om = new ObjectMapper();
            String customerJson = om.writeValueAsString(customer);
            s3Client.putObject(BUCKET, customerKey, customerJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public Customer get(Long id) {
        try {
            String customerKey = PREFIX + id;
            S3Object object = s3Client.getObject(BUCKET, customerKey);
            byte[] objectBytes = object.getObjectContent().readAllBytes();
            ObjectMapper om = new ObjectMapper();
            return om.readValue(objectBytes, Customer.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void remove(Long id) {
        String customerKey = PREFIX + id;
        s3Client.deleteObject(BUCKET, customerKey);
    }

    public List<Customer> list() {
        List<Customer> customersList = new ArrayList<>();
        List<S3ObjectSummary> objects = s3Client.listObjects(BUCKET, PREFIX).getObjectSummaries();

        for (S3ObjectSummary o : objects) {
            try {
                Long customerId = Long.parseLong(o.getKey().substring(PREFIX.length()));
                Customer customer = get(customerId);
                if (customer != null) {
                    customersList.add(customer);
                }
            } catch (Exception e) {
                // Handle errors
            }
        }
        return customersList;
    }
}
