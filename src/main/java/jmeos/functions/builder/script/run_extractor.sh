#!/bin/bash
cd ..
javac -d ./tmp/classes ./BuilderLibrary.java ./FunctionsExtractor.java
java -cp ./tmp/classes jmeos.functions.builder.FunctionsExtractor
read -p "End of extraction."
