package de.tdlabs.apps.screencaster.screencast.grabbing;

import de.tdlabs.apps.screencaster.ScreenCasterProperties;

import java.awt.*;

public abstract class AbstractScreenGrabber implements ScreenGrabber{

  private final Rectangle screenRect;

  private final GraphicsDevice screen;

  public AbstractScreenGrabber(ScreenCasterProperties screenCastProperties) {

    ScreenCasterProperties.ScreenGrabbingProperties screenGrabbingProperties = screenCastProperties.getGrabbing();
    this.screen = selectScreenDevice(screenGrabbingProperties);
    this.screenRect = getScreenRectangle(screen);
}

  private Rectangle getScreenRectangle(GraphicsDevice screen) {
    DisplayMode displayMode = screen.getDisplayMode();
    return new Rectangle(displayMode.getWidth(), displayMode.getHeight());
  }

  private GraphicsDevice selectScreenDevice(ScreenCasterProperties.ScreenGrabbingProperties screenGrabbingProperties) {

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

  public Rectangle getScreenRect() {
    return screenRect;
  }

  public GraphicsDevice getScreen() {
    return screen;
  }
}
