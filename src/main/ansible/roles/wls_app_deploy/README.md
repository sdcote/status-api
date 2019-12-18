wls_app_deploy


# Examples
The following deploys the application

```
    - hosts: wlsserver
      
      roles:
        - wls_app_deploy
          deploy_action: "undeploy"
```