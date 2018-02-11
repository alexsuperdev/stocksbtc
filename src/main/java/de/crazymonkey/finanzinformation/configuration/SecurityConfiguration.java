package de.crazymonkey.finanzinformation.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Value("${security.userrole}")
	private String userrole;

	@Value("${security.username}")
	private String userName;

	@Value("${security.userpassword}")
	private String userPassword;

	private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder authenticationMgr) throws Exception {
		authenticationMgr.inMemoryAuthentication().withUser(userName).password(userPassword).roles(userrole);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		logger.info(" start authorize");
		// http.csrf().disable().cors().disable().authorizeRequests().antMatchers("/api/**").authenticated().and().httpBasic().and()
		// .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// http.csrf().disable().cors().disable().authorizeRequests().antMatchers("/api/getSymbol").permitAll().and()
		// .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// http.csrf().disable().cors().disable().anonymous().and().antMatcher("/api/getSharePrices").
		// .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// http.csrf().disable().cors().disable();

	}

	// TODO allowedOrigins as property

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				// registry.addMapping("**/api/**").allowedOrigins("http://localhost:3000");
				registry.addMapping("/**");
			}
		};
	}
}
