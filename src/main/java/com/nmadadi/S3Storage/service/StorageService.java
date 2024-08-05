package com.nmadadi.S3Storage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StorageService {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 s3Client;

    public StorageService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public List<String> listObjects() {
        ObjectListing objectListing = s3Client.listObjects(bucketName);
        return objectListing.getObjectSummaries()
                .stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }

    public void uploadFile(MultipartFile file) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        s3Client.putObject(new PutObjectRequest(bucketName, file.getOriginalFilename(), file.getInputStream(), metadata));
    }

    public byte[] downloadFile(String fileName) {
        S3Object s3Object= s3Client.getObject(new GetObjectRequest(bucketName, fileName));
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteFile(String fileName) {
        s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }
}









