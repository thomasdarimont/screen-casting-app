package de.tdlabs.apps.screencaster.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Configuration
@EnableScheduling
@EnableJpaAuditing
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
class MainConfig {
}
