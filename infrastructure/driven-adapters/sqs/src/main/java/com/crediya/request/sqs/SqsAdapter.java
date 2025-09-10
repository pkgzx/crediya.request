package com.crediya.request.sqs;

    import com.crediya.request.model.notification.LoanApplicationStatusChangedEvent;
    import com.crediya.request.model.notification.spi.INotificationClient;
    import com.crediya.request.sqs.config.SqsProperties;
    import com.google.gson.Gson;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Component;
    import reactor.core.publisher.Mono;
    import software.amazon.awssdk.services.sqs.SqsClient;
    import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
    import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
    import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

    @Slf4j
    @Component
    @RequiredArgsConstructor
    public class SqsAdapter implements INotificationClient {
      @Value("${aws.sqs.queueName}")
      private String queueName;
      private String queueUrl = null;
      private final SqsClient sqsClient;
      private final Gson gson = new Gson();

      @Override
      public Mono<Void> sendNotification(LoanApplicationStatusChangedEvent notificacion) {
        String messageJson = gson.toJson(notificacion);
        log.info("JSON: {}", messageJson);
        String queueUrl = getQueueUrl();

        return Mono.fromCallable(() -> {
            SendMessageRequest request = SendMessageRequest.builder()
              .queueUrl(queueUrl)
              .messageBody(messageJson)
              .build();
            SendMessageResponse response = sqsClient.sendMessage(request);
            log.info("Mensaje enviado a SQS con id: {}", response.messageId());
            return response;
          })
          .doOnError(err -> log.error("Error enviando mensaje a SQS", err))
          .then();
      }

      private String getQueueUrl() {
        if (queueUrl == null) {
          GetQueueUrlRequest request = GetQueueUrlRequest.builder()
            .queueName(queueName)
            .build();
          queueUrl = sqsClient.getQueueUrl(request).queueUrl();
        }
        return queueUrl;
      }
    }