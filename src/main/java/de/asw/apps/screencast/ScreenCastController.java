package de.asw.apps.screencast;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class ScreenCastController {

	private final ScreenFetcher screenFetcher;

	@GetMapping(path = "/screenshot", produces = MediaType.IMAGE_JPEG_VALUE)
	byte[] getScreenshot() {
		return screenFetcher.getCurrentImage();
	}
}
