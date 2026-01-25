#!/bin/bash

# Define the classpath
SHODRONE_CP="shodrone.app.backoffice\target\shodrone.app.backoffice-0.1.0.jar;shodrone.app.backoffice\target\dependency\*"

# Call the java VM
java -cp $SHODRONE_CP lapr4.ShodroneBackoffice

# Pause the script (optional, for debugging purposes)
read -p "Press any key to continue... " -n1 -s
