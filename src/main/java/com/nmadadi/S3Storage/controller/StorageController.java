package com.nmadadi.S3Storage.controller;

import com.nmadadi.S3Storage.service.StorageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        byte[] data = storageService.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestBody MultipartFile file) {
        try {
            storageService.uploadFile(file);
            return new ResponseEntity<>("Object uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error creating object: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("delete/{fileName}")
    public ResponseEntity<String> deleteObject(@PathVariable("fileName") String fileName) {
        storageService.deleteFile(fileName);
        return new ResponseEntity<>("Object deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/files")
    public ResponseEntity<List<String>> listObjects() {
        List<String> objectKeys = storageService.listObjects();
        return new ResponseEntity<>(objectKeys, HttpStatus.OK);
    }


}
