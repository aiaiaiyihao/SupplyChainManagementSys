package org.yihao.productserver.Service;

import io.awspring.cloud.s3.S3Operations;
import io.awspring.cloud.s3.S3Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.yihao.shared.DTOS.DownloadResponse;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@Slf4j
public class StorageService {
    @Value("${application.bucket.name}")
    private String bucketName;
    @Value("${spring.cloud.aws.region.static}")
    private String region;


    private static final List<String> users = Arrays.asList(
            "Alice", "Bob", "Catherine", "David", "Elis", "Freddy", "Gary", "Hill"
    );

    private static Map<String, String> contentTypeMap = new HashMap<>();

    private final S3Client s3Client;

    private final S3Operations s3Operations;

    @Autowired
    public StorageService(S3Client s3Client, S3Operations s3Operations) {
        this.s3Client = s3Client;
        this.s3Operations = s3Operations;
    }

    public String uploadFile(MultipartFile file,String userEmail) throws IOException {
        InputStream is = new BufferedInputStream(file.getInputStream());
        String key  = userEmail + "_" + file.getOriginalFilename();
        S3Resource upload = this.s3Operations.upload(bucketName, key, is);
        contentTypeMap.put(key, file.getContentType());
        log.info("File uploaded successfully..{}", upload.getLocation());
        // Construct the actual S3 URL
        return key;
    }

    public byte[] downloadFile(String fileName) {
        GetObjectRequest.Builder getObjectRequestBuilder = GetObjectRequest.builder();
        getObjectRequestBuilder
                .bucket(bucketName)
                .key(fileName);

        ResponseBytes<GetObjectResponse> response = s3Client.getObjectAsBytes(getObjectRequestBuilder.build());
        log.info("Object response {}", response.toString());
        return response.asByteArray();
    }

    public DownloadResponse downloadFileEnhanced(String fileName) {
        GetObjectRequest.Builder getObjectRequestBuilder = GetObjectRequest.builder();
        getObjectRequestBuilder
                .bucket(bucketName)
                .key(fileName);

        ResponseBytes<GetObjectResponse> response = s3Client.getObjectAsBytes(getObjectRequestBuilder.build());
        log.info("Object response {}", response.toString());
        return DownloadResponse.builder()
                .contentType(contentTypeMap.get(fileName))
                .data(response.asByteArray())
                .build();
    }

    public String deleteFile(String fileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        DeleteObjectResponse response = s3Client.deleteObject(deleteObjectRequest);
        log.info("Deleted object {} from bucket {}", fileName, bucketName);

        return "Deleted file=[" + fileName +"] success";
    }

    private String getCurrentUser() {
        int randomIdx = new Random().nextInt(users.size());
        return users.get(randomIdx);
    }
}
