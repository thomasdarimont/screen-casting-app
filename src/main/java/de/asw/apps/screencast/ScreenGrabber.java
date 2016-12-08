package de.asw.apps.screencast;

import java.awt.AWTException;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

@Component
class ScreenGrabber {

	private final Robot robot;
	private final Rectangle screenRect;

	public ScreenGrabber() {

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		DisplayMode displayMode = ge.getDefaultScreenDevice().getDisplayMode();
		this.screenRect = new Rectangle(0, 0, displayMode.getWidth(), displayMode.getHeight());

		try {
			this.robot = new Robot();
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}
	}

	public BufferedImage grab() {
		return grab(this.screenRect);
	}

	public BufferedImage grab(Rectangle screenRect) {
		return robot.createScreenCapture(screenRect);
	}

	public byte[] grabAsBytes() {

		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024 * 400);
		try {
			ImageIO.write(grab(), "jpg", baos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

}
