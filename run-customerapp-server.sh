#!/bin/bash

# Define the classpath
SHODRONE_CP="shodrone.server.customerapp/target/shodrone.server.customerapp-0.1.0.jar:shodrone.server.customerapp/target/dependency/*"

# Execute the Java Customer App Server Daemon
java -cp "$SHODRONE_CP" lapr4.daemon.cas.CustomerAppServerDaemon -port:8080

# Pause the script (optional, for debugging purposes)
read -p "Press any key to continue... " -n1 -s
