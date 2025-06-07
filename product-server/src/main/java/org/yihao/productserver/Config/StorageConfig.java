package org.yihao.productserver.Config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Slf4j
public class StorageConfig {

    @Autowired
    private Secrets secrets;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Autowired
    public StorageConfig(Secrets secrets) {
        this.secrets = secrets;
    }

    @Bean
    public AwsCredentials awsCredentials() {
        return AwsBasicCredentials.create(secrets.getProperty("access_key"), secrets.getProperty("secret_key"));
    }

    @Bean
    public Region awsRegion() {
        return Region.of(region);
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .credentialsProvider(this::awsCredentials)
                .region(awsRegion())
                .build();
    }
}
