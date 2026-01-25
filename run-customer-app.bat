REM set the class path,
REM assumes the build was executed with maven copy-dependencies
SET SHODRONE_CP=shodrone.app.customer\target\shodrone.app.customer-0.1.0.jar;shodrone.app.customer\target\dependency\*;

REM call the java VM, e.g, 
java -cp %SHODRONE_CP% lapr4.app.customer.console.ShodroneCustomerApp

pause