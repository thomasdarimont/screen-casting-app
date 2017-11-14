package de.tdlabs.apps.screencaster.tray;

import de.tdlabs.apps.screencaster.ScreenCasterProperties;
import de.tdlabs.apps.screencaster.settings.SettingsService;
import dorkbox.systemTray.Menu;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.SystemTray;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Component
@RequiredArgsConstructor
class SystemTrayRegistrar {

  private final SettingsService settingsService;

  private final ScreenCasterProperties screenCasterProperties;

  private final ConfigurableApplicationContext applicationContext;

  @PostConstruct
  public void init() {


    SystemTray systemTray = SystemTray.get();
    systemTray.setTooltip("Screen Caster");

    toggleTrayStatus(systemTray, screenCasterProperties.getScreencast().isAutoStart());

    Menu menu = systemTray.getMenu();

    menu.add(new MenuItem("Enable Screencast", (e) -> {
      settingsService.enableCast();
      toggleTrayStatus(systemTray, true);
    }));

    menu.add(new MenuItem("Disable Screencast", (e) -> {
      settingsService.disableCast();
      toggleTrayStatus(systemTray, false);
    }));

    menu.add(new MenuItem("Quit", (e) -> {
      settingsService.disableCast();
      applicationContext.close();
      System.exit(0);
    }));
  }

  @PreDestroy
  public void destroy() {
    SystemTray systemTray = SystemTray.get();
    if (systemTray != null) {
      systemTray.getMenu().clear();
    }
  }

  private void toggleTrayStatus(SystemTray systemTray, boolean enabled) {

    if (enabled) {
      systemTray.setImage(ImageResources.CAST_ENABLED_ICON);
      systemTray.setStatus("Screencast enabled");
    } else {
      systemTray.setImage(ImageResources.CAST_DISABLED_ICON);
      systemTray.setStatus("Screencast disabled");
    }

    systemTray.setTooltip(systemTray.getStatus());
  }

  static class ImageResources {

    private static final BufferedImage CAST_ENABLED_ICON;
    private static final BufferedImage CAST_DISABLED_ICON;

    static {
      try {
        CAST_ENABLED_ICON = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream("static/img/ball-blue.png"));
        CAST_DISABLED_ICON = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream("static/img/ball-gray.png"));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
