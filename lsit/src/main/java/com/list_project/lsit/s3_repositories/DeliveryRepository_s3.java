package com.list_project.lsit.s3_repositories;

import java.util.*;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.list_project.lsit.Models.DeliveryDriver;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Repository
public class DeliveryRepository_s3 {
    private static final String BUCKET = "lsit-project";
    private static final String PREFIX = "delivery/";
    private static final String ACCESS_KEY = "AKIATG6MGQSAYOUKHH6Q";
    private static final String SECRET_KEY = "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";
    private final AmazonS3 s3Client;

    public DeliveryRepository_s3() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        s3Client = AmazonS3ClientBuilder.standard()
                .withRegion("eu-north-1")
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    public void add(DeliveryDriver driver) {
        try {
            driver.setId(UUID.randomUUID().getMostSignificantBits());
            ObjectMapper objectMapper = new ObjectMapper();
            String driverJson = objectMapper.writeValueAsString(driver);
            s3Client.putObject(BUCKET, PREFIX + driver.getId(), driverJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public DeliveryDriver get(String id) {
        try {
            S3Object object = s3Client.getObject(BUCKET, PREFIX + id);
            byte[] objectBytes = object.getObjectContent().readAllBytes();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(objectBytes, DeliveryDriver.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void remove(String id) {
        s3Client.deleteObject(BUCKET, PREFIX + id);
    }

    public List<DeliveryDriver> list() {
        List<DeliveryDriver> drivers = new ArrayList<>();
        List<S3ObjectSummary> objects = s3Client.listObjects(BUCKET, PREFIX).getObjectSummaries();

        for (S3ObjectSummary o : objects) {
            try {
                String driverId = o.getKey().substring(PREFIX.length());
                DeliveryDriver driver = get(driverId);
                if (driver != null) {
                    drivers.add(driver);
                }
            } catch (Exception e) {
                // Handle errors
            }
        }
        return drivers;
    }
}
