package com.github.frankfarrell.multiplexor.springboot.servlet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.frankfarrell.multiplexor.springboot.model.MultiplexorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

/**
 * Created by ffarrell on 23/02/2018.
 */
@Component
public class MultiplexorServlet extends HttpServlet {

    private final DispatcherServlet dispatcherServlet;
    private final ObjectMapper objectMapper;

    @Autowired
    public MultiplexorServlet(final DispatcherServlet dispatcherServlet, final ObjectMapper objectMapper) {
        this.dispatcherServlet = dispatcherServlet;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void service(final HttpServletRequest servletRequest,
                           final HttpServletResponse servletResponse)
            throws ServletException, IOException {

        final List<MultiplexorRequest> requests = objectMapper.readValue(servletRequest.getInputStream(), new TypeReference<List<MultiplexorRequest>>(){});



        dispatcherServlet.service(servletRequest, servletResponse);
    }
}
