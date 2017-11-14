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
  private final boolean showMouse;

  public ScreenGrabber(ScreenCasterProperties screenCastProperties) {

    ScreenGrabbingProperties screenGrabbingProperties = screenCastProperties.getGrabbing();
    GraphicsDevice screen = selectScreenDevice(screenGrabbingProperties);
    this.screenRect = getScreenRectangle(screen);
    this.showMouse = screenCastProperties.getScreencast().isMouseVisible();

    try {
      this.robot = new Robot(screen);
    } catch (AWTException e) {
      throw new RuntimeException(e);
    }
  }

  private Rectangle getScreenRectangle(GraphicsDevice screen) {
    DisplayMode displayMode = screen.getDisplayMode();
    return new Rectangle(displayMode.getWidth(), displayMode.getHeight());
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

    BufferedImage screenCapture = robot.createScreenCapture(screenRect);

    if (showMouse) {
      drawMousePointer(screenCapture);
    }

    return screenCapture;
  }

  private void drawMousePointer(BufferedImage screenCapture) {

    Point location = MouseInfo.getPointerInfo().getLocation();
    Graphics2D g = screenCapture.createGraphics();
    g.setColor(Color.MAGENTA);
    g.fillOval((int) location.getX(), (int) location.getY(), 32, 32);
  }
}
