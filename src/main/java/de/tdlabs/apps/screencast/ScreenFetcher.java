package de.tdlabs.apps.screencast;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
class ScreenFetcher {

  private final ScreenGrabber screenGrabber;

  private final Settings settings;

  private final AtomicReference<byte[]> currentImage = new AtomicReference<>();

  @Scheduled(fixedDelayString = "#{${screencast.refreshIntervalMillis:-1}}")
  void updateImage() {

    if(!settings.isCastEnabled()){
      return;
    }

    currentImage.set(screenGrabber.grabAsBytes());
  }

  byte[] getCurrentImage() {
    return currentImage.get();
  }
}
