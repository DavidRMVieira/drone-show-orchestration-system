REM set the class path,
REM assumes the build was executed with maven copy-dependencies
SET SHODRONE_CP=shodrone.app.testing\target\shodrone.app.testing-0.1.0.jar;shodrone.app.testing\target\dependency\*;

REM call the java VM, e.g, 
java -cp %SHODRONE_CP% lapr4.app.testing.console.ShodroneTestingApp

pause