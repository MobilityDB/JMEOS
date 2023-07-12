@echo off
cd ..
javac -d .\tmp\classes .\FunctionsBuilder.java
java -cp .\tmp\classes function.builder.FunctionsBuilder
pause