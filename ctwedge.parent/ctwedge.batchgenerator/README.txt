README for CTWedge installation:
* Compile ctwedge.web with Maven, to export it as .war, rename it as "ctwedge.war" and load it into Tomcat
  - The url should be /ctwedge (the index.html is called)
  - The generator REST API is deployed at /ctwedge/generator (the servlet is inside that .war)

* Export ctwedge.batchgenerator as Executable JAR, and put it into /var/www/foselab_html/ctwedge/ with name "batchgenerator.jar" (it must have executable permissions)
  - in that folder, there should be also the folders "done", "todo", and "results"
  - in that folder (/var/www/foselab_html/ctwedge) place also the CASA executable (and in general all the generation external executables), in particular for Linux is "casa-1.1b".
  - that batchgenerator.jar should be run as a service in the system (only one instance).
  - every 1 second, it checks whether there is anything to process in the folder "todo", and puts the model in "done", and the generated test suite in "results".
    - in the current configuration, the service description is under /etc/systemd/system/ctwedge_batchgenerator.service
    - the service command is the script /var/www/foselab_html/ctwedge/batchgenerator.sh, that just runs the jar, and also starts the apache and tomcat services in the server. Since we have seen that just the start command is not enough, it then waits 30 seconds, and restart all of them. Below are pasted the scripts contents and locations.
    
* /etc/systemd/system/ctwedge_batchgenerator.service:
Description=CTWedge BatchGenerator JAR
[Service]
User=root
# The configuration file application.properties should be here:
#change this to your workspace
WorkingDirectory=/var/www/foselab_html/ctwedge
#path to executable.
#executable is a bash script which calls jar file
ExecStart=/var/www/foselab_html/ctwedge/batchgenerator.sh
SuccessExitStatus=143
TimeoutStopSec=10
Restart=on-failure
RestartSec=5
[Install]
WantedBy=multi-user.target

* /var/www/foselab_html/ctwedge/batchgenerator.sh:
#!/bin/sh
sudo java -jar batchgenerator.jar
sudo /usr/local/sbin/startTomcat.sh
sleep 30
sudo /usr/local/sbin/restartAllApacheAndTomcat.sh

* /usr/local/sbin/startTomcat.sh:
#!/bin/bash
/opt/tomcat/apache-tomcat-8.5.42/bin/shutdown.sh
/opt/tomcat/apache-tomcat-8.5.42/bin/startup.sh
/opt/tomcat/apache-tomcat-8.5.42b/bin/shutdown.sh
/opt/tomcat/apache-tomcat-8.5.42b/bin/startup.sh
service apache2 start
service apache2@se4med start
./restartAllApacheAndTomcat.sh

* /usr/local/sbin/restartAllApacheAndTomcat.sh:
#!/bin/bash
/opt/tomcat/apache-tomcat-8.5.42/bin/shutdown.sh
/opt/tomcat/apache-tomcat-8.5.42/bin/startup.sh
/opt/tomcat/apache-tomcat-8.5.42b/bin/shutdown.sh
/opt/tomcat/apache-tomcat-8.5.42b/bin/startup.sh
service apache2 restart
service apache2@se4med restart