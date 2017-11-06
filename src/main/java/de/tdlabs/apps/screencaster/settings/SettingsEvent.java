package de.tdlabs.apps.screencaster.settings;

import lombok.Data;

public interface SettingsEvent {

  default String getType() {
    return getClass().getSimpleName().toLowerCase();
  }

  static SettingsEvent updated(Settings settings) {
    return new SettingsEvent.Updated(settings);
  }

  @Data
  class Updated implements SettingsEvent {
    final Settings settings;
  }
}