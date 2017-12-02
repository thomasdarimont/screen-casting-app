package de.tdlabs.apps.screencaster.screencast.grabbing.ffmpeg;

import de.tdlabs.apps.screencaster.ScreenCasterProperties;
import de.tdlabs.apps.screencaster.ScreenCasterProperties.ScreenGrabbingProperties.X11Properties;
import de.tdlabs.apps.screencaster.screencast.grabbing.AbstractScreenGrabber;
import org.apache.commons.lang3.SystemUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.awt.*;
import java.awt.image.BufferedImage;

@Profile("ffmpeg")
@Primary
@Component
class FfmpegScreenGrabber extends AbstractScreenGrabber {

  private final FFmpegFrameGrabber grabber;

  private final Java2DFrameConverter frameConverter;

  public FfmpegScreenGrabber(ScreenCasterProperties screenCastProperties) {
    super(screenCastProperties);

    Rectangle screenRect = getScreenRect();

    this.grabber = createFrameGrabber(screenCastProperties, screenRect);

    this.grabber.setImageWidth((int) screenRect.getWidth());
    this.grabber.setImageHeight((int) screenRect.getHeight());

    this.frameConverter = new Java2DFrameConverter();
  }

  private FFmpegFrameGrabber createFrameGrabber(ScreenCasterProperties screenCastProperties, Rectangle screenRect) {

    if (SystemUtils.IS_OS_LINUX) {
      X11Properties x11 = screenCastProperties.getGrabbing().getX11();
      FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(x11.getDisplay() + "+" + (int) screenRect.getX() + "," + (int) screenRect.getY());
      grabber.setFormat("x11grab");
      return grabber;
    }

    if (SystemUtils.IS_OS_WINDOWS) {
      FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("screen-capture-recorder");
      grabber.setFormat("dshow");
      return grabber;
    }

    throw new UnsupportedOperationException("Screen grabbing with ffmpeg is currently not supported on this platform.");
  }

  @PostConstruct
  void start() {
    try {
      grabber.start();
    } catch (FrameGrabber.Exception e) {
      e.printStackTrace();
    }
  }

  @PreDestroy
  void destroy() {
    try {
      grabber.stop();
    } catch (FrameGrabber.Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public BufferedImage grab() {

    try {
      Frame frame = grabber.grab();

      return frameConverter.convert(frame);

    } catch (FrameGrabber.Exception e) {
      e.printStackTrace();
    }

    return null;
  }
}
