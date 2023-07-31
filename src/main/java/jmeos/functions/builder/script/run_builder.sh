#!/bin/bash

# Check if the environment variable containing the path exists
if [ -z "$JMEOS_HOME" ]; then
    echo "This script requires administrative privileges to define JMEOS_HOME."
    echo ""
    echo "The JMEOS_HOME environment variable is not defined. It is the absolute path of the Java project in MobilityDB-JMEOS."
    read -p "Enter JMEOS_HOME value: " JMEOS_HOME
    echo ""
    echo "Saving JMEOS_HOME in user environment variables..."
    echo ""
    export JMEOS_HOME="$JMEOS_HOME"
fi

# Go to project directory
cd "$JMEOS_HOME"

# Compile Java File
javac -d target/classes src/main/java/jmeos/utils/*.java src/main/java/jmeos/functions/builder/FunctionsBuilder.java
java -cp target/classes jmeos.functions.builder.FunctionsBuilder

# Check if compilation was successful
if [ $? -ne 0 ]; then
    echo "Error in Java compilation."
fi

# End of script
read -p "End of build."
