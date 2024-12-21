package com.list_project.lsit.Services;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.AmazonServiceException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
@Slf4j
public class S3Service {
    private final AmazonS3 s3Client;
    private final String bucketName;

    public S3Service(AmazonS3 s3Client, 
                    @Value("${aws.s3.bucket}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public void uploadFile(String key, byte[] content) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(content.length);
            
            s3Client.putObject(bucketName, key, 
                new ByteArrayInputStream(content), metadata);
            
            log.info("File uploaded successfully: {}", key);
        } catch (AmazonServiceException e) {
            log.error("Error uploading file: {}", e.getMessage());
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public byte[] downloadFile(String key) {
        try {
            S3Object object = s3Client.getObject(bucketName, key);
            return IOUtils.toByteArray(object.getObjectContent());
        } catch (IOException | AmazonServiceException e) {
            log.error("Error downloading file: {}", e.getMessage());
            throw new RuntimeException("Failed to download file", e);
        }
    }

    public void deleteFile(String key) {
        try {
            s3Client.deleteObject(bucketName, key);
            log.info("File deleted successfully: {}", key);
        } catch (AmazonServiceException e) {
            log.error("Error deleting file: {}", e.getMessage());
            throw new RuntimeException("Failed to delete file", e);
        }
    }
} 