package de.tdlabs.apps.screencaster.screencast.grabbing.awt;

import de.tdlabs.apps.screencaster.ScreenCasterProperties;
import de.tdlabs.apps.screencaster.screencast.grabbing.AbstractScreenGrabber;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.awt.image.BufferedImage;

@Component
class AwtScreenGrabber extends AbstractScreenGrabber {

  private Robot robot;

  public AwtScreenGrabber(ScreenCasterProperties screenCastProperties) {
    super(screenCastProperties);
  }

  @PostConstruct
  public void start(){
    try {
      this.robot = new Robot(getScreen());
    } catch (AWTException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public BufferedImage grab() {
    return robot.createScreenCapture(getScreenRect());
  }
}
