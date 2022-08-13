package com.sourav.resulttracker.configuration;

import javax.annotation.PostConstruct;

import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeleniumConfig {

	@Value("${web.chromedriver.path}")
	String chormeDriverPath;

	@Bean
	public ChromeDriver driver() {
		return new ChromeDriver();
	}

	@PostConstruct
	public void postConstruct() {
		System.setProperty("webdriver.chrome.driver", chormeDriverPath);
	}
}
