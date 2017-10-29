package de.tdlabs.apps.screencast.screen;

import de.tdlabs.apps.screencast.ScreenCastProperties;
import de.tdlabs.apps.screencast.ScreenCastProperties.ScreenGrabbingProperties;
import org.springframework.stereotype.Component;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Component
class ScreenGrabber {

  private final Robot robot;
  private final Rectangle screenRect;
  private final ScreenGrabbingProperties screenGrabbingProperties;

  public ScreenGrabber(ScreenCastProperties screenCastProperties) {

    this.screenGrabbingProperties = screenCastProperties.getGrabbing();

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
    if (screenNo < 0 || screenNo > screenDevices.length) {
      throw new IllegalArgumentException("invalid screenNo: " + screenNo);
    }

    return screenDevices[screenNo];
  }

  byte[] grabAsBytes() {
    return toJpegImageAsBytes(grab());
  }

  private BufferedImage grab() {
    return grab(this.screenRect);
  }

  private BufferedImage grab(Rectangle screenRect) {
    return robot.createScreenCapture(screenRect);
  }

  private byte[] toJpegImageAsBytes(BufferedImage image) {

    ByteArrayOutputStream baos = new ByteArrayOutputStream(1024 * 400);

    ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
    try {
      jpgWriter.setOutput(new MemoryCacheImageOutputStream(baos));
      IIOImage outputImage = new IIOImage(image, null, null);

      ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
      jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
      jpgWriteParam.setCompressionQuality(screenGrabbingProperties.getQuality());

      jpgWriter.write(null, outputImage, jpgWriteParam);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      jpgWriter.dispose();
    }

    return baos.toByteArray();
  }
}
