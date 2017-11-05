package de.tdlabs.apps.screencaster.screencast;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class ScreenCastController {

  private final ScreenCastService screenShotService;

  @GetMapping(path = "/screenshot.jpg", produces = MediaType.IMAGE_JPEG_VALUE)
  byte[] getScreenshot() {
    return screenShotService.getLatestScreenShotImageBytes();
  }
}
