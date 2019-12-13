package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class HomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "text/plain")
    public String test() {
        StringBuffer sb = new StringBuffer();
        sb.append("Hostname: ");
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            sb.append(hostname);
        } catch (UnknownHostException e) {
            sb.append("UNKNOWN");
        } finally {
            sb.append("\n");
        }
        return sb.toString();
    }

}
