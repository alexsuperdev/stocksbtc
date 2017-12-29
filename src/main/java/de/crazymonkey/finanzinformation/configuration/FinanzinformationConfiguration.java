package de.crazymonkey.finanzinformation.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = "de.crazymonkey.finanzinformation")
public class FinanzinformationConfiguration {

	@Bean
	public RestTemplate getYahooTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}
}
