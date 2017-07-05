package de.tdlabs.apps.screencast;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class WebApi {

  private final ScreenFetcher screenFetcher;

  @GetMapping(path = "/screenshot", produces = MediaType.IMAGE_JPEG_VALUE)
  byte[] getScreenshot() {
    return screenFetcher.getCurrentImage();
  }
}
