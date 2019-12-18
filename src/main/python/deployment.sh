# deployment.sh [Optional]
echo '***** WELCOME TO DEPLOYMENT SCRIPT *****'
user=`echo $LOGNAME`
echo 'APPLICATION DEPLOYMENT IS STARTING IN INSTANCE:' $user
DHOME=/Home/domains/wd$LOGNAME/config
WLHOST=`hostname`
cd $DHOME
WLPORT=`cat config.xml |grep listen-port|tr -s [:blank:] |tr " " "\n" |grep listen-port |cut -c14-17`
URL='t3://'$WLHOST':'$WLPORT
DENV=$HOME/domains/wd$LOGNAME/bin/setDomainEnv.sh
. $DENV $*
echo $CLASSPATH
#calling the deployment.py file
java weblogic.WLST -loadProperties /Home/$user/.Admin/deploy.properties /Home/$user/.Admin/deployment.py $user $URL$CLASSPATH

