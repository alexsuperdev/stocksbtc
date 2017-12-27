package de.crazymonkey.finanzinformation.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;

@Configuration
//@EnableAspectJAutoProxy
public class FinanzinformationConfiguration {

	@Value("${alphavantage.key}")
	private String alphavantageKey;

	@Bean
	public RestTemplate getYahooTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		// DefaultUriTemplateHandler handler = new DefaultUriTemplateHandler();
		// restTemplate.setUriTemplateHandler(handler);
		// restTemplate.setInterceptors(Collections.singletonList(new
		// AlphavantageInterceptos(alphavantageKey)));
		// List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		// messageConverters.add(MappingJackson2HttpMessageConverter.class);
		// restTemplate.setMessageConverters(messageConverters);
		return restTemplate;
	}
}
