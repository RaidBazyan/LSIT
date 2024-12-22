package com.list_project.lsit.Services;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import java.io.IOException;

@Configuration
public class StorageConfig {
    @Value("${gcp.project.id}")
    private String projectId;

    @Value("${gcp.credentials.location}")
    private String credentialsPath;

    private final ResourceLoader resourceLoader;

    public StorageConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public Storage storage() throws IOException {
        Resource credentialsResource = resourceLoader.getResource(credentialsPath);
        return StorageOptions.newBuilder()
            .setProjectId(projectId)
            .setCredentials(GoogleCredentials.fromStream(credentialsResource.getInputStream()))
            .build()
            .getService();
    }
} 