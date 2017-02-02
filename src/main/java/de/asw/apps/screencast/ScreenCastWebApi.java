package de.asw.apps.screencast;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.Inet4Address;

@RestController
@RequiredArgsConstructor
class ScreenCastWebApi {

  private final ScreenFetcher screenFetcher;

  @GetMapping(path = "/screenshot", produces = MediaType.IMAGE_JPEG_VALUE)
  byte[] getScreenshot() {
    return screenFetcher.getCurrentImage();
  }
}
