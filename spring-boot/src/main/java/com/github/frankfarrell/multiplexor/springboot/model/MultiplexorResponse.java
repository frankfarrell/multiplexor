package com.github.frankfarrell.multiplexor.springboot.model;

import java.util.Map;
import java.util.Optional;

/**
 * Created by ffarrell on 23/02/2018.
 */
public class MultiplexorResponse {

    public final String statusCode;
    public final Optional<String> body;
    public final Optional<Map<String, String>> headers;


    public MultiplexorResponse(String statusCode, String body, Map<String, String> headers) {
        this.statusCode = statusCode;
        this.body = Optional.ofNullable(body);
        this.headers = Optional.ofNullable(headers);
    }
}
