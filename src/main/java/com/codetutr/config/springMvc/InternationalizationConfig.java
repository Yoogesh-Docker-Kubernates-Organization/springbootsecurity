package com.codetutr.config.springMvc;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

public class InternationalizationConfig {

	@Bean
	public SessionLocaleResolver sessionLocaleResolver() {
		SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
		Locale defaultLocale = Locale.forLanguageTag("en_US");
		sessionLocaleResolver.setDefaultLocale(defaultLocale);
		return sessionLocaleResolver;
	}
}
