package de.tdlabs.apps.screencaster.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import static de.tdlabs.apps.screencaster.config.WebsocketDestinations.TOPIC_NOTES;
import static de.tdlabs.apps.screencaster.config.WebsocketDestinations.TOPIC_POINTER;
import static de.tdlabs.apps.screencaster.config.WebsocketDestinations.TOPIC_SETTINGS;

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker(TOPIC_NOTES, TOPIC_SETTINGS, TOPIC_POINTER);
    config.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/screencaster/ws").withSockJS();
  }
}