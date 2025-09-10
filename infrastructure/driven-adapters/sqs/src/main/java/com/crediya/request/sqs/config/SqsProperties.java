package com.crediya.request.sqs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws.sqs")
public record SqsProperties(
  String region,
  String accessKey,
  String secretKey){
}