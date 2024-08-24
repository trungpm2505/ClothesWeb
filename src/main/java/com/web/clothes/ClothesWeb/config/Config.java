package com.web.clothes.ClothesWeb.config;


import javax.servlet.MultipartConfigElement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.context.MessageSource;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import java.util.Locale;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class Config implements WebMvcConfigurer{
	 @Bean
	 public MultipartConfigElement multipartConfigElement() {
	     return new MultipartConfigElement("");
	 }

	 @Bean
	 public MultipartResolver multipartResolver() {
	     org.springframework.web.multipart.commons.CommonsMultipartResolver multipartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver();
	     multipartResolver.setMaxUploadSize(1000000);
	     return multipartResolver;
	 }
	 
	 //language
	 @Bean
	    public LocaleResolver localeResolver() {
	        SessionLocaleResolver slr = new SessionLocaleResolver();
	        slr.setDefaultLocale(new Locale("en"));
	        return slr;
	    }

	    @Bean
	    public LocaleChangeInterceptor localeChangeInterceptor() {
	        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
	        lci.setParamName("lang");
	        return lci;
	    }

	    @Override
	    public void addInterceptors(InterceptorRegistry registry) {
	        registry.addInterceptor(localeChangeInterceptor());
	    }

	    @Bean
	    public MessageSource messageSource() {
	        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	        messageSource.setBasename("messages/message");
	        messageSource.setDefaultEncoding("UTF-8");
	        return messageSource;
	    }

	
	  
}
