package de.tdlabs.apps.screencast;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
class SettingsForm {

  volatile boolean castEnabled;
}
