package com.list_project.lsit.Controllers;

import com.list_project.lsit.Services.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/storage")
@Slf4j
public class StorageController {
    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("path") String path) {
        try {
            String key = path + "/" + file.getOriginalFilename();
            storageService.uploadFile(key, file.getBytes());
            return ResponseEntity.ok("File uploaded successfully: " + key);
        } catch (IOException e) {
            log.error("Error processing file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file");
        }
    }

    @GetMapping("/download/{path}/{filename}")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable String path,
            @PathVariable String filename) {
        try {
            String key = path + "/" + filename;
            byte[] content = storageService.downloadFile(key);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "attachment; filename=\"" + filename + "\"")
                    .body(content);
        } catch (Exception e) {
            log.error("Error downloading file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{path}/{filename}")
    public ResponseEntity<String> deleteFile(
            @PathVariable String path,
            @PathVariable String filename) {
        try {
            String key = path + "/" + filename;
            storageService.deleteFile(key);
            return ResponseEntity.ok("File deleted successfully: " + key);
        } catch (Exception e) {
            log.error("Error deleting file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete file");
        }
    }
} 