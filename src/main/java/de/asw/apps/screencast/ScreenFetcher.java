package de.asw.apps.screencast;

import java.util.concurrent.atomic.AtomicReference;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class ScreenFetcher {

	private final ScreenGrabber screenGrabber;

	private final AtomicReference<byte[]> currentImage = new AtomicReference<>();

	@Scheduled(fixedDelay = 250)
	void updateImage() {
		currentImage.set(screenGrabber.grabAsBytes());
	}

	byte[] getCurrentImage() {
		return currentImage.get();
	}
}
