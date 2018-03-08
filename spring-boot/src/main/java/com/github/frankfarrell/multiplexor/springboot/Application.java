package com.github.frankfarrell.multiplexor.springboot;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.github.frankfarrell.multiplexor.springboot.servlet.MultiplexorServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ffarrell on 23/02/2018.
 */
@SpringBootApplication
public class Application {


    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public MultiplexorServlet multiplexorServlet(DispatcherServlet dispatcherServlet, ObjectMapper objectMapper){
        return new MultiplexorServlet(dispatcherServlet, objectMapper);
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(final MultiplexorServlet multiplexorServlet){
        final ServletRegistrationBean registration = new ServletRegistrationBean(multiplexorServlet,"/multiplexor");
        return registration;
    }


    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    @Bean
    public ServletRegistrationBean dispatcherServletRegistration(DispatcherServlet dispatcherServlet) {

        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
        registration.setLoadOnStartup(0);
        registration.setName(
                DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);

        return registration;
    }


    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        return Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .modules(jdk8Module());
    }

    @Bean
    public Module jdk8Module() {
        return new Jdk8Module();
    }
}
