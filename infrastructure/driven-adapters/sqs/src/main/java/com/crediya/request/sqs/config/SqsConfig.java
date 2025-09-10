package com.crediya.request.sqs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

@Configuration
public class SqsConfig {

  @Bean
  public SqsClient amazonSqsClient(SqsProperties sqsProperties) {
    return SqsClient.builder()
      .region(Region.of(sqsProperties.region()))
      .credentialsProvider(
        StaticCredentialsProvider.create(
          AwsBasicCredentials.create(
            sqsProperties.accessKey(),
            sqsProperties.secretKey()
          )
        )
      )
      .build();
  }
}