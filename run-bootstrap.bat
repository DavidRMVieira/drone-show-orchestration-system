REM set the class path,
REM assumes the build was executed with maven copy-dependencies
SET SHODRONE_CP=shodrone.app.bootstrap\target\shodrone.app.bootstrap-0.1.0.jar;shodrone.app.bootstrap\target\dependency\*;

REM call the java VM, e.g,
java -cp %SHODRONE_CP% lapr4.ShodroneBootstrap -bootstrap:demo -smoke:basic -smoke:e2e

pause