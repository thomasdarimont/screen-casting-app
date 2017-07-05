package de.tdlabs.apps.screencast.web;

import de.tdlabs.apps.screencast.screen.ScreenController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class WebApi {

  private final ScreenController screenController;

  @GetMapping(path = "/screenshot", produces = MediaType.IMAGE_JPEG_VALUE)
  byte[] getScreenshot() {
    return screenController.getLatestScreenImageBytes();
  }
}
