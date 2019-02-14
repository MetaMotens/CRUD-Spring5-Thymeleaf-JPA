package com.bolsadeideas.springboot.app;

import java.nio.file.Paths;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class McvConfig implements WebMvcConfigurer
{
	private final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());//podemos mostrar en la consola y hacer un debug
	
	/*public void addResourceHandlers(ResourceHandlerRegistry registry) {

		String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();//touri toma el path y le agrega el esquema file
		log.info(resourcePath);
		
		registry.addResourceHandler("/uploads/**").addResourceLocations(resourcePath);
	}*/
	
}
