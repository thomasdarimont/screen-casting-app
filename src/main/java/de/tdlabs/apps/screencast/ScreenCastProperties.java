package de.tdlabs.apps.screencast;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "screencast")
public class ScreenCastProperties {

  private ScreenGrabbingProperties grabbing = new ScreenGrabbingProperties();

  @Data
  public static class ScreenGrabbingProperties {

    public static final int DEFAULT_SCREEN = -1;

    public static final float DEFAULT_QUALITY = 0.7f;

    /**
     * Selects the screen to grab. {@literal -1} means default screen.
     */
    private int screenNo = DEFAULT_SCREEN;

    /**
     * Selects the default quality. Must be between {@literal 0.0} and {@literal 1.0}.
     */
    private float quality = DEFAULT_QUALITY;

    public boolean isGrabDefaultScreen() {
      return screenNo == DEFAULT_SCREEN;
    }
  }
}
