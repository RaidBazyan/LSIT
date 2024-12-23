package com.list_project.lsit.Services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
class StorageServiceIntegrationTest {

    @Autowired
    private StorageService storageService;

    private final String testKey = "integration-test-file.txt";
    private final byte[] testContent = "Integration Test Content".getBytes();

    @Test
    void testUploadDownloadFile() {
        storageService.uploadFile(testKey, testContent);
        byte[] downloadedContent = storageService.downloadFile(testKey);

        assertArrayEquals(testContent, downloadedContent);
        storageService.deleteFile(testKey);
    }

    @Test
    void testListFiles() {
        String prefix = "integration-test/";
        storageService.uploadFile(prefix + "file1.txt", "File 1".getBytes());
        storageService.uploadFile(prefix + "file2.txt", "File 2".getBytes());

        List<String> files = storageService.listFiles(prefix);
        assertTrue(files.contains(prefix + "file1.txt"));
        assertTrue(files.contains(prefix + "file2.txt"));

        storageService.deleteFile(prefix + "file1.txt");
        storageService.deleteFile(prefix + "file2.txt");
    }

    @Test
    void testDeleteFile() {
        storageService.uploadFile(testKey, testContent);
        storageService.deleteFile(testKey);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> storageService.downloadFile(testKey));
        assertTrue(exception.getMessage().contains("File not found"));
    }
}
