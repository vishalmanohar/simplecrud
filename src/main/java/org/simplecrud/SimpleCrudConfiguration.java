package org.simplecrud;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackageClasses = org.simplecrud.SimpleCrudConfiguration.class)
@EnableWebMvc
public class SimpleCrudConfiguration extends WebMvcConfigurerAdapter {


    @Bean(name = "paginatorViewResolver")
    public ViewResolver paginatorViewResolver(){
        FreeMarkerViewResolver freeMarkerViewResolver = new FreeMarkerViewResolver();
        freeMarkerViewResolver.setCache(true);
        freeMarkerViewResolver.setPrefix("");
        freeMarkerViewResolver.setSuffix(".ftl");
        freeMarkerViewResolver.setExposeSpringMacroHelpers(true);
        return freeMarkerViewResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/simplecrud/**").addResourceLocations("classpath:/simplecrud/");
    }

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer(Environment environment){
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setPreferFileSystemAccess(false);
        Map variables = new HashMap();
        String contextPath = environment.getProperty("server.contextPath");
        variables.put("contextRoot", contextPath == null ? "" : contextPath);
        freeMarkerConfigurer.setFreemarkerVariables(variables);
        freeMarkerConfigurer.setTemplateLoaderPath("classpath:/views/");
        return freeMarkerConfigurer;
    }
}
