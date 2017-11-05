package de.tdlabs.apps.screencaster;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Settings {

  public Settings(ScreenCasterProperties screenCasterProperties) {
    this.castEnabled = screenCasterProperties.getScreencast().isAutoStart();
  }

  volatile boolean castEnabled;
}
