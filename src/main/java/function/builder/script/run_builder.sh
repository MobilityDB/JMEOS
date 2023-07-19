#!/bin/bash
cd ..
javac -d ./tmp/classes ./FunctionsBuilder.java
java -cp ./tmp/classes function.builder.FunctionsBuilder
read -p "End of function generation."
