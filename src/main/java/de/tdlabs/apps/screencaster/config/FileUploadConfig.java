package de.tdlabs.apps.screencaster.config;

import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.MultipartConfigElement;

@Configuration
@EnableConfigurationProperties(MultipartProperties.class)
class FileUploadConfig {


  @Bean(name = "multipartResolver")
  public CommonsMultipartResolver multipartResolver(MultipartProperties multipartProperties) {

    MultipartConfigElement multipartConfig = multipartProperties.createMultipartConfig();

    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
    multipartResolver.setMaxUploadSize(multipartConfig.getMaxFileSize());
    return multipartResolver;
  }
}
