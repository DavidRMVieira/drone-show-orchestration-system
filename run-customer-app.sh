#!/bin/bash

# Define the classpath
SHODRONE_CP="shodrone.app.customer/target/shodrone.app.customer-0.1.0.jar:shodrone.app.customer/target/dependency/*"

# Call the java VM
java -cp $SHODRONE_CP lapr4.ShodroneCustomerApp

# Pause the script (optional, for debugging purposes)
read -p "Press any key to continue... " -n1 -s
