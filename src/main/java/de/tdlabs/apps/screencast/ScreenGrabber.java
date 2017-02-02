package de.tdlabs.apps.screencast;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

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

  BufferedImage grab() {
    return grab(this.screenRect);
  }

  BufferedImage grab(Rectangle screenRect) {
    return robot.createScreenCapture(screenRect);
  }

  byte[] grabAsBytes() {

    ByteArrayOutputStream baos = new ByteArrayOutputStream(1024 * 400);
    try {
      ImageIO.write(grab(), "jpg", baos);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return baos.toByteArray();
  }
}
