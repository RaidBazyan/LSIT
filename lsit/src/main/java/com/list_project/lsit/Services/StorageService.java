package com.list_project.lsit.Services;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class StorageService {
    private final Storage storage;
    private final String bucketName;

    public StorageService(Storage storage, 
                         @Value("${gcp.bucket.name}") String bucketName) {
        this.storage = storage;
        this.bucketName = bucketName;
    }

    public void uploadFile(String key, byte[] content) {
        try {
            BlobId blobId = BlobId.of(bucketName, key);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            storage.create(blobInfo, content);
            log.info("File uploaded successfully: {}", key);
        } catch (StorageException e) {
            log.error("Error uploading file: {}", e.getMessage());
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public byte[] downloadFile(String key) {
        try {
            Blob blob = storage.get(BlobId.of(bucketName, key));
            if (blob == null) {
                throw new RuntimeException("File not found: " + key);
            }
            return blob.getContent();
        } catch (StorageException e) {
            log.error("Error downloading file: {}", e.getMessage());
            throw new RuntimeException("Failed to download file", e);
        }
    }

    public void deleteFile(String key) {
        try {
            BlobId blobId = BlobId.of(bucketName, key);
            boolean deleted = storage.delete(blobId);
            if (deleted) {
                log.info("File deleted successfully: {}", key);
            } else {
                log.warn("File not found: {}", key);
            }
        } catch (StorageException e) {
            log.error("Error deleting file: {}", e.getMessage());
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    public List<String> listFiles(String prefix) {
        List<String> fileList = new ArrayList<>();
        Page<Blob> blobs = storage.list(bucketName, Storage.BlobListOption.prefix(prefix));
        
        for (Blob blob : blobs.iterateAll()) {
            fileList.add(blob.getName());
        }
        return fileList;
    }
} 