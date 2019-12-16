package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * Proof of Concept only requires status. A more complete service will include Version and deployment information such
 * as hostname on which the instance is running to support additional use cases of recovering partial deployments and
 * verifying loa balancing.
 */
@RestController
public class AppStatusController {

    private static final String STATUS = "status";
    private static final String VERSION = "version";
    private static final String UNKNOWN = "unknown";

    String state = "stand-by";

    @Value("${appversion}")
    private String version;


    private String getState() {
        if (StringUtils.isEmpty(state))
            return UNKNOWN;
        else
            return state;
    }

    private String getVersion() {
        if (StringUtils.isEmpty(version))
            return UNKNOWN;
        else
            return version;
    }

    private Map<String, String> getStatusMap() {
        Map<String, String> retval = new HashMap<>();
        retval.put(STATUS, getState());
        retval.put(VERSION, getVersion());
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

