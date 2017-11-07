package de.tdlabs.apps.screencaster.screencast;

import de.tdlabs.apps.screencaster.ScreenCasterProperties;
import de.tdlabs.apps.screencaster.settings.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
class SimpleScreenCastService implements ScreenCastService {

  private final ScreenGrabber screenGrabber;

  private final SettingsService settingsService;

  private final ScreenCasterProperties screenCasterProperties;

  private final AtomicReference<LiveImage> currentImage = new AtomicReference<>();

  @Scheduled(fixedDelayString = "#{${screencaster.refreshIntervalMillis:-1}}")
  void updateImage() {

    if (!settingsService.isCastEnabled()) {
      usePauseImage();
      return;
    }

    useLiveImage();
  }

  private void useLiveImage() {
    currentImage.lazySet(new LiveImage(screenGrabber.grab(), screenCasterProperties.getGrabbing().getQuality()));
  }

  private void usePauseImage() {

    LiveImage current = currentImage.get();
    if (current instanceof PauseImage) {
      return;
    }

    currentImage.lazySet(new PauseImage("Paused...", current));
  }

  public byte[] getLatestScreenShotImageBytes() {
    return currentImage.get().getBytes();
  }

  static class LiveImage {

    final BufferedImage screenshot;
    final float quality;

    byte[] bytes;

    public LiveImage(BufferedImage screenshot, float quality) {
      this.screenshot = screenshot;
      this.quality = quality;
    }

    public byte[] getBytes() {

      if (bytes != null) {
        return bytes;
      }

      bytes = toJpegImageAsBytes(getScreenshot());

      return bytes;
    }

    public BufferedImage getScreenshot() {
      return screenshot;
    }

    private byte[] toJpegImageAsBytes(BufferedImage image) {

      ByteArrayOutputStream baos = new ByteArrayOutputStream(1024 * 400);

      ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
      try {
        jpgWriter.setOutput(new MemoryCacheImageOutputStream(baos));
        IIOImage outputImage = new IIOImage(image, null, null);

        ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
        jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpgWriteParam.setCompressionQuality(quality);

        jpgWriter.write(null, outputImage, jpgWriteParam);
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        jpgWriter.dispose();
      }

      return baos.toByteArray();
    }
  }

  static class PauseImage extends LiveImage {

    private final String text;

    public PauseImage(String text, LiveImage liveImage) {
      super(liveImage.screenshot, liveImage.quality);
      this.text = text;
    }

    @Override
    public BufferedImage getScreenshot() {

      BufferedImage pauseScreenshot = super.getScreenshot();

      Graphics2D g = (Graphics2D) pauseScreenshot.getGraphics();

      Font font = new Font(g.getFont().getName(), Font.BOLD, 96);

      FontMetrics metrics = g.getFontMetrics(font);
      g.setColor(Color.MAGENTA);

      int x = (pauseScreenshot.getWidth() - metrics.stringWidth(text)) / 2;
      int y = ((pauseScreenshot.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();

      g.setFont(font);
      g.drawString(text, x, y);
      g.dispose();

      return pauseScreenshot;
    }
  }
}
