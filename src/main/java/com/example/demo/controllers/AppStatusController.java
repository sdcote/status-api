package com.example.demo.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
public class AppStatusController {

    private static final String STATUS = "status";
    String state = "stand-by";

    private String getState() {
        return state;
    }

    private Map<String, String> getStatusMap() {
        Map<String, String> retval = new HashMap<>();
        retval.put(STATUS, getState());
        return retval;
    }

    // GET calls are public, all others require AuthN & AuthZ
    @GetMapping(path = "/appstatus", produces = "application/json")
    public Map<String, String> getStatus() {
        return getStatusMap();
    }

    // Only those with the ADMIN role can POST to this endpoint
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/appstatus", consumes = "application/json", produces = "application/json")
    public Map<String, String> setStatus(@RequestBody Map state) {
        // Validate and update the state
        return getStatusMap();
    }

}

