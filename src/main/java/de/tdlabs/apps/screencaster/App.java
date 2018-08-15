package de.tdlabs.apps.screencaster;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EnableConfigurationProperties(ScreenCasterProperties.class)
public class App {

  @Value("${server.port}")
  String serverPort;

  String scheme = "http";

  public static void main(String[] args) {

    System.setProperty("java.awt.headless", "false");

    SpringApplication.run(App.class, args);
  }

  @EventListener
  public void run(ApplicationReadyEvent are) {

    String hostName = tryResolveHostnameWithFallbackToLocalhost();
    String ipAddress = tryGetIpAddressWithFallbackToLoopback();

    System.out.println("########################################################>");
    System.out.printf("####### Screencast URLs%n");
    System.out.printf("####### %s://%s:%s/%n", scheme, hostName, serverPort);
    System.out.printf("####### %s://%s:%s/%n", scheme, ipAddress, serverPort);
    System.out.println("########################################################>");
  }

  private String tryResolveHostnameWithFallbackToLocalhost() {

    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException uhe) {
      System.err.printf("%s. Trying IP based configuration...%n", uhe.getMessage());
      return "localhost";
    }
  }

  private String tryGetIpAddressWithFallbackToLoopback() {
    try {
      return InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException innerUhe) {
      System.err.printf("Could not determine host address. Using 127.0.0.1 as fallback...%n");
      System.err.println("You need to determine the hostname yourself!");
      return "127.0.0.1";
    }
  }
}
