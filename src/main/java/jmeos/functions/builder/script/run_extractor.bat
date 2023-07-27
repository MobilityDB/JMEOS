@echo off
cd ..
javac -d .\tmp\classes .\BuilderLibrary.java .\FunctionsExtractor.java
java -cp .\tmp\classes jmeos.functions.builder.FunctionsExtractor
echo End of extraction.
pause