package de.tdlabs.apps.screencast;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Configuration

@EnableScheduling
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
class Config {
}
