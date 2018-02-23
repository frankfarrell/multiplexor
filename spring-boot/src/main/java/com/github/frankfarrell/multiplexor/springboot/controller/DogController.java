package com.github.frankfarrell.multiplexor.springboot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ffarrell on 23/02/2018.
 */
@RestController
@RequestMapping("/dog")
public class DogController {

    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<String>> postDownloadUsage(){
        return new ResponseEntity<>(Arrays.asList("labrador", "pooch", "poodle"), HttpStatus.OK);
    }
}
