package com.github.frankfarrell.multiplexor.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.frankfarrell.multiplexor.springboot.servlet.MultiplexorServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
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
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
