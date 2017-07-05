package de.tdlabs.apps.screencast;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Settings {

  volatile boolean castEnabled = true;
}
