#!/bin/bash
cd ..
javac -d ./tmp/classes ./BuilderLibrary.java ./FunctionsBuilder.java
java -cp ./tmp/classes jmeos.functions.builder.FunctionsBuilder
read -p "End of function generation."
