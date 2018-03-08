package com.github.frankfarrell.multiplexor.springboot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ffarrell on 23/02/2018.
 */
@RestController
@RequestMapping("/dog")
public class DogController {

    private final ArrayList<String> dogs = new ArrayList<>(Arrays.asList("labrador", "pooch", "poodle"));

    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ArrayList<String>> getDogs(){
        return new ResponseEntity<>(dogs, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ArrayList<String>> addDog(@RequestBody final String newDog){
        dogs.add(newDog);
        return new ResponseEntity<>(dogs, HttpStatus.CREATED);
    }
}
