package com.example.demo.controllers;

import com.example.demo.AppState;
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
 *
 */
@RestController
public class AppStatusController {

    private static final String STATUS = "status";
    private static final String VERSION = "version";

    @Value("${appversion}")
    private String version;


    private String getVersion() {
        if (StringUtils.isEmpty(version))
            return AppState.UNKNOWN;
        else
            return version;
    }

    private Map<String, String> getStatusMap() {
        Map<String, String> retval = new HashMap<>();
        retval.put(STATUS, AppState.getState());
        retval.put(VERSION, getVersion());
        return retval;
    }

    @GetMapping(path = "/appstatus", produces = "application/json")
    public Map<String, String> getStatus() {
        return getStatusMap();
    }

    @PostMapping(path = "/appstatus", consumes = "application/json", produces = "application/json")
    public Map<String, String> setStatus(@RequestBody Map data) {
        AppState.LOGGER.debug("Received a POST request to set status to " + data);
        AppState.setState("stand-by");
        return getStatusMap();
    }

}

