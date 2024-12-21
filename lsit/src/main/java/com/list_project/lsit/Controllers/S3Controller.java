package com.list_project.lsit.Controllers;

import com.list_project.lsit.Services.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/s3")
@Slf4j
public class S3Controller {
    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("path") String path) {
        try {
            String key = path + "/" + file.getOriginalFilename();
            s3Service.uploadFile(key, file.getBytes());
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
            byte[] content = s3Service.downloadFile(key);
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
            s3Service.deleteFile(key);
            return ResponseEntity.ok("File deleted successfully: " + key);
        } catch (Exception e) {
            log.error("Error deleting file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete file");
        }
    }
} 