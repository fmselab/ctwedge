README for CTWedge installation:
* Compile ctwedge.web with Maven, to export it as .war, rename it as "ctwedge.war" and load it into Tomcat
  - The url should be /ctwedge (the index.html is called)
  - The generator REST API is deployed at /ctwedge/generator (the servlet is inside that .war)

* Export ctwedge.batchgenerator as Executable JAR, and put it into /var/www/foselab_html/ctwedge/ with name "batchgenerator.jar" (it must have executable permissions)
  - in that folder, there should be also the folders "done", "todo", and "results"
  - in that folder (/var/www/foselab_html/ctwedge) place also the CASA executable (and in general all the generation external executables), in particular for Linux is "casa-1.1b".
