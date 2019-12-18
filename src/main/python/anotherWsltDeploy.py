
def wlDeploy(username, password, adminURL, appName, appPath):
    try:
        #connect to admin server
        connect(username, password, adminURL)

        #start edit operation
        edit()
        startEdit()

        #start deploying application to admin server
        progress = deploy(appName,appPath,upload='true')
        progress.printStatus()
        save()
        activate(20000,block="true")

        #disconnect from Admin server
        disconnect()
        exit()
    except Exception, ex:
        print ex.toString()
        cancelEdit('y')

#wlDeploy("weblogic","weblogic1","t3://slc07fic.us.oracle.com:7001","myAppName", "/app/restService.war")
wlDeploy("<Admin User Name>","<Password>","<AdminUrl","<App Name>", "<Path of war file>")
