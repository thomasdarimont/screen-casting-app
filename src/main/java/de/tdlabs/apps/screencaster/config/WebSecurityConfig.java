package de.tdlabs.apps.screencaster.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
      .authorizeRequests()
      .anyRequest().permitAll()
      .and().headers().frameOptions().deny()
//      .and()
//      .formLogin().and()
//      .httpBasic()
    ;
  }
}
