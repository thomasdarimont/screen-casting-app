package de.asw.apps.screencast;

import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

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

		String hostName = InetAddress.getLocalHost().getHostName();
		System.out.println("########################################################>");
		System.out.printf("####### ScreenCasting running under: http://%s:%s/%n", hostName, serverPort);
		System.out.println("########################################################>");
	}
}
