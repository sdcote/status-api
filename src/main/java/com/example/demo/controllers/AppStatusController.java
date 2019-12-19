package com.example.demo.controllers;

import com.example.demo.AppStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * This is the AppStatus controller which simply displays the current status and version of the application when
 * requested by an anonymous GET request to the "/appstatus" wndpoint.
 *
 * <p>The status can be changed by sending a POST request to the "/appstatus" endpoint with the name-value pair of
 * "status":"[value]" in JSON format. Only 3 status values are supported: "ready","stage" and "stand-by". Any other
 * value submitted will result in a state of "stand-by" being set.</p>
 *
 * <p>Setting of the status should be protected in Spring Security configurations limiting access to the "POST" method
 * on the "/appstatus" endpoint.</p>
 */
@RestController
public class AppStatusController {

    private static final String STATUS = "status";
    private static final String VERSION = "version";
    private static final String UNKNOWN = "unknown";
    private static final String ERROR = "error";
    private static final String NO_STATUS_VALUE = "No status value provided";


    @Value("${appversion}")
    private String version;


    /**
     * @return the value of the version application property
     */
    private String getVersion() {
        if (StringUtils.isEmpty(version))
            return UNKNOWN;
        else
            return version;
    }


    /**
     * @return the map of status data for this instance
     */
    private Map<String, String> getStatusMap() {
        Map<String, String> retval = new HashMap<>();
        retval.put(STATUS, AppStatus.getStatus());
        retval.put(VERSION, getVersion());
        return retval;
    }


    /**
     * @return the name of the user currently authenticated
     */
    private String getCurrentUsername() {
        String retval;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            retval = ((UserDetails) principal).getUsername();
        } else {
            retval = principal.toString();
        }
        return retval;
    }


    /**
     * @return the map of status data for this instance
     */
    @GetMapping(path = "/appstatus", produces = "application/json")
    public Map<String, String> getStatus() {
        return getStatusMap();
    }


    /**
     * @param data     the JSON data sent to this endpoint as a Map
     * @param response the response to which we can write if errors occur
     * @return the map of status data for this instance
     */
    @PostMapping(path = "/appstatus", consumes = "application/json", produces = "application/json")
    public Map<String, String> setStatus(@RequestBody Map data, HttpServletResponse response) {
        Map<String, String> retval;
        String status = null;
        for (Object key : data.keySet()) {
            if (STATUS.equalsIgnoreCase(key.toString())) {
                status = data.get(key).toString().toLowerCase();
                break;
            }
        }
        if (status != null) {
            AppStatus.LOGGER.info("User '" + getCurrentUsername() + "' requested setting the status to '" + status + "'");
            AppStatus.setStatus(status);
            retval = getStatusMap();
            AppStatus.LOGGER.info("AppStatus currently set to '" + AppStatus.getStatus() + "'");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            retval = new HashMap<>();
            retval.put(ERROR, NO_STATUS_VALUE);
        }
        return retval;
    }

}

