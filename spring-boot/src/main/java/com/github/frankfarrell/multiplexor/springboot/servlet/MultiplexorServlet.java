package com.github.frankfarrell.multiplexor.springboot.servlet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.frankfarrell.multiplexor.springboot.model.MultiplexorRequest;
import com.github.frankfarrell.multiplexor.springboot.model.MultiplexorResponse;
import com.github.frankfarrell.multiplexor.springboot.request.DeMultiplexedHttpServletRequest;
import com.github.frankfarrell.multiplexor.springboot.request.DeMultiplexedHttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import static java.util.stream.Collectors.toList;

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

        long totalStartTime = System.currentTimeMillis();

        final List<MultiplexorResponse> responses = requests.stream()
                .parallel()
                .map(request -> {
                    final DeMultiplexedHttpServletResponse httpResponse = new DeMultiplexedHttpServletResponse(new CountDownLatch(1));

                    long startTime = System.currentTimeMillis();
                    try {
                        dispatcherServlet.service(new DeMultiplexedHttpServletRequest(request), httpResponse);
                    } catch (ServletException|IOException e) {
                        //TODO ?
                    }

                    long endTime = System.currentTimeMillis();

                    return new MultiplexorResponse(request.method, request.path, httpResponse, objectMapper, endTime -startTime);
                })
                .collect(toList());

        long totalEndTime = System.currentTimeMillis();

        servletResponse.setHeader("totalDuration", Long.toString(totalEndTime-totalStartTime));
        servletResponse.getWriter().append(objectMapper.writeValueAsString(responses));
        servletResponse.setStatus(207);
        servletResponse.setContentType("application/json");
    }
}
