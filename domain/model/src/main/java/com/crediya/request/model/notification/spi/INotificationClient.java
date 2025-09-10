package com.crediya.request.model.notification.spi;

import com.crediya.request.model.notification.LoanApplicationStatusChangedEvent;
import reactor.core.publisher.Mono;

public interface INotificationClient {
  Mono<Void> sendNotification(LoanApplicationStatusChangedEvent notification);
}
