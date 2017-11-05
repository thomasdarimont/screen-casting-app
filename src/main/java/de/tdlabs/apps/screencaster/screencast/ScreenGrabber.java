package de.tdlabs.apps.screencaster.screencast;

import de.tdlabs.apps.screencaster.ScreenCasterProperties;
import de.tdlabs.apps.screencaster.ScreenCasterProperties.ScreenGrabbingProperties;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;

@Component
class ScreenGrabber {

  private final Robot robot;
  private final Rectangle screenRect;

  public ScreenGrabber(ScreenCasterProperties screenCastProperties) {

    ScreenGrabbingProperties screenGrabbingProperties = screenCastProperties.getGrabbing();

    GraphicsDevice screen = selectScreenDevice(screenGrabbingProperties);

    DisplayMode displayMode = screen.getDisplayMode();
    this.screenRect = new Rectangle(0, 0, displayMode.getWidth(), displayMode.getHeight());

    try {
      this.robot = new Robot(screen);
    } catch (AWTException e) {
      throw new RuntimeException(e);
    }
  }

  private GraphicsDevice selectScreenDevice(ScreenGrabbingProperties screenGrabbingProperties) {

    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    if (screenGrabbingProperties.isGrabDefaultScreen()) {
      return ge.getDefaultScreenDevice();
    }

    GraphicsDevice[] screenDevices = ge.getScreenDevices();

    int screenNo = screenGrabbingProperties.getScreenNo();
    if (screenNo < 0 || screenNo >= screenDevices.length) {
      throw new IllegalArgumentException("invalid screenNo: " + screenNo);
    }

    return screenDevices[screenNo];
  }

  public BufferedImage grab() {
    return grab(this.screenRect);
  }

  private BufferedImage grab(Rectangle screenRect) {
    return robot.createScreenCapture(screenRect);
  }
}
