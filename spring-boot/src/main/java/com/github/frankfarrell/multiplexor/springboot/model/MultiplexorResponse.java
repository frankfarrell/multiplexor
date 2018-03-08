package com.github.frankfarrell.multiplexor.springboot.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.frankfarrell.multiplexor.springboot.request.DeMultiplexedHttpServletResponse;
import org.springframework.http.HttpMethod;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

/**
 * Created by ffarrell on 23/02/2018.
 */
public class MultiplexorResponse {

    public final HttpMethod requestMethod;
    public final String requestPath;
    public final String statusCode;
    public final Optional<String> body;
    public final Optional<Map<String, String>> headers;


    public MultiplexorResponse(HttpMethod requestMethod, String requestPath, String statusCode, String body, Map<String, String> headers) {
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
        this.statusCode = statusCode;
        this.body = Optional.ofNullable(body);
        this.headers = Optional.ofNullable(headers);
    }

    public MultiplexorResponse(HttpMethod requestMethod, String requestPath,
                               DeMultiplexedHttpServletResponse response,
                               ObjectMapper objectMapper){
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
        this.statusCode = String.valueOf(response.getStatus());
        this.body = Optional.ofNullable(response.getAwsResponseBodyString());
        this.headers = Optional.ofNullable(response.getHeaderNames().stream().collect(toMap(Function.identity(), response::getHeader)));
    }
}
