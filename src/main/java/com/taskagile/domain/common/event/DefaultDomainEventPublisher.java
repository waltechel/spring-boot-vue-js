package com.taskagile.domain.common.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * The default implementation of DomainEventPublisher that
 * bases on Spring Application Event
 */
@Component
@RequiredArgsConstructor
public class DefaultDomainEventPublisher implements DomainEventPublisher {

  private final ApplicationEventPublisher actualPublisher;

  @Override
  public void publish(DomainEvent event) {
    actualPublisher.publishEvent(event);
  }

}