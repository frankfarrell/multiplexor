package com.github.frankfarrell.multiplexor.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpMethod;

import java.util.Map;
import java.util.Optional;

/**
 * Created by frankfarrell on 23/02/2018.
 */
public class MultiplexorRequest {

    public final HttpMethod method;
    public final String path;
    public final Optional<Map<String, String>> queryParams;
    public final Optional<Map<String, String>> headers;
    public final Optional<String> body;

    @JsonCreator
    public MultiplexorRequest(@JsonProperty(value = "method", required = true) final HttpMethod method,
                              @JsonProperty(value = "path", required = true) final String path,
                              @JsonProperty(value = "queryParams", required = false) final Map<String, String> queryParams,
                              @JsonProperty(value = "headers", required = false) final Map<String, String> headers,
                              @JsonProperty(value = "body", required = false) final String body) {
        this.method = method;
        this.path = path;
        this.queryParams = Optional.ofNullable(queryParams);
        this.headers = Optional.ofNullable(headers);
        this.body = Optional.ofNullable(body);
    }
}
