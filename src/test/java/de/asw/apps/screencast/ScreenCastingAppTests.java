package de.asw.apps.screencast;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScreenCastingAppTests {

	static {
		System.setProperty("java.awt.headless", "false");
	}
	
	@Test
	public void contextLoads() {
	}

}
