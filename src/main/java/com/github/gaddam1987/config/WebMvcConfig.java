package com.github.gaddam1987.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@ComponentScan(basePackages = "com.github.gaddam1987")
public class WebMvcConfig extends WebMvcConfigurationSupport {
}
