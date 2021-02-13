package ru.oshokin.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig extends WebMvcAutoConfiguration {

//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        if (!registry.hasMappingForPattern("/webjars/**")) {
//            registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//        }
//    }

}
