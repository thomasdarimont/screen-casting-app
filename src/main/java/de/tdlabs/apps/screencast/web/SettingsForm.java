package de.tdlabs.apps.screencast.web;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
class SettingsForm {

  boolean castEnabled;
}
