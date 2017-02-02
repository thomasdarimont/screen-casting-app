package de.tdlabs.apps.screencast;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;

@EnableScheduling
@SpringBootApplication
public class ScreenCastApp {

  @Value("${server.port}")
  String serverPort;

  public static void main(String[] args) {

    System.setProperty("java.awt.headless", "false");

    SpringApplication.run(ScreenCastApp.class, args);
  }

  @EventListener
  public void run(ApplicationReadyEvent are) throws Exception {

    String hostName = tryResolveHostnameWithFallbackToLocalhost();

    System.out.println("########################################################>");
    System.out.printf("####### ScreenCasting running under: http://%s:%s/%n", hostName, serverPort);
    System.out.println("########################################################>");
  }

  private String tryResolveHostnameWithFallbackToLocalhost() {

    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException uhe) {
      System.err.printf("%s. Trying IP based configuration...%n", uhe.getMessage());
      try {
        return InetAddress.getLocalHost().getHostAddress();
      } catch (UnknownHostException innerUhe) {
        System.err.printf("%s. Using localhost as fallback...%n", uhe.getMessage());
        System.err.println("You need to determine the hostname yourself!");
        return "localhost";
      }
    }
  }
}
