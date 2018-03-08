package com.github.frankfarrell.multiplexor.springboot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ffarrell on 23/02/2018.
 */
@RestController
@RequestMapping("/cat")
public class CatController {

    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<String>> getCats(){
        return new ResponseEntity<>(Arrays.asList("lion", "jaguar", "tiger"),HttpStatus.OK);
    }

}
