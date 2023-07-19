#!/bin/bash
cd ..
javac -d ./tmp/classes ./FunctionsExtractor.java
java -cp ./tmp/classes function.builder.FunctionsExtractor
read -p "End of extraction."
