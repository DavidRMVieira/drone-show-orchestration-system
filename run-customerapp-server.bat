REM set the class path,
REM assumes the build was executed with maven copy-dependencies
SET SHODRONE_JAR=shodrone.server.customerapp\target\shodrone.server.customerapp-0.1.0.jar
SET SHODRONE_CP=%SHODRONE_JAR%;shodrone.server.customerapp\target\dependency\*;


IF EXIST "%SHODRONE_JAR%" (
	echo Running Customer App Server...
	java -cp %SHODRONE_CP% lapr4.daemon.cas.CustomerAppServerDaemon -port:8080
) ELSE (
	echo [ERROR] Customer App Server JAR not found!
	echo Please build first: mvn clean package -pl shodrone.server.customerapp -am
)

REM call the java VM, e.g,

pause

