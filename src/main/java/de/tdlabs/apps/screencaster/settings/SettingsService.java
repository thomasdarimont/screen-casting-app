package de.tdlabs.apps.screencaster.settings;

import de.tdlabs.apps.screencaster.ScreenCasterProperties;
import de.tdlabs.apps.screencaster.config.WebsocketDestinations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class SettingsService {

  private final SimpMessagingTemplate messagingTemplate;

  private Settings settings = new Settings();

  public SettingsService(SimpMessagingTemplate messagingTemplate, ScreenCasterProperties screenCasterProperties) {
    this.messagingTemplate = messagingTemplate;
    this.settings.setCastEnabled(screenCasterProperties.getScreencast().isAutoStart());
  }

  public boolean isCastEnabled() {
    return settings.isCastEnabled();
  }

  public void enableCast() {
    this.settings.setCastEnabled(true);
    publishSettingsUpdate();
  }

  public void disableCast() {
    this.settings.setCastEnabled(false);
    Executors.newSingleThreadScheduledExecutor() //
      .schedule(this::publishSettingsUpdate, 1500L, TimeUnit.MILLISECONDS);
  }

  void publishSettingsUpdate() {
    messagingTemplate.convertAndSend(WebsocketDestinations.TOPIC_SETTINGS, SettingsEvent.updated(settings));
  }
}
