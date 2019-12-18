import re

warPath = '<location of war/ear>'

connect('<user - weblogic>', '<password>', '<server-url>')

appList = re.findall('<app name>#(\d*)', ls('/AppDeployments'))
if len(appList) > 1:
    oldestArchiveVersion = min(map(int, appList))
    undeploy('<app name>', archiveVersion = oldestArchiveVersion)

deploy('<app name>', path = warPath, retireTimeout = 0, upload = 'True')

exit()