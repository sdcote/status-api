package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;


@RestController
public class AppStatusController {

    private static final String STATUS = "status";
    String state = "stage";

    @GetMapping(path = "/appstatus", produces = "application/json")
    public Map<String, String> getStatus() {
        Map<String, String> retval = new HashMap<>();
        retval.put(STATUS, getState());
        return retval;
    }

    private String getState() {
        return state;
    }

    @PostMapping(path = "/appstatus", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> setStatus(@RequestBody Map state) {
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand("boo")
                .toUri();

        return ResponseEntity.created(location).build();
    }
}

