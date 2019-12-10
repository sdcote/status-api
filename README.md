# Overview
This is a simple Spring Boot web service endpoint that is used to test the load balancing for an F5 configuration.  This configuration allows traffic to be sent to ony those web sites whose "appstatus" matches the configured health check rules.

When a development team wants traffic to be sent to an application instance, it simply changes its status to match that of the health check rule. This allows for the incremental deployment of application instances. 