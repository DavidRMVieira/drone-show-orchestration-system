REM set the class path,
REM assumes the build was executed with maven copy-dependencies
SET SHODRONE_CP=shodrone.server.customerapp\target\shodrone.server.customerapp-0.1.0.jar;shodrone.server.customerapp\target\dependency\*;

REM call the java VM, e.g,
java -cp %SHODRONE_CP% lapr4.daemon.cas.CustomerAppServerDaemon -port:8080

pause

