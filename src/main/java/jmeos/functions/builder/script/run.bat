@echo off

echo Extracting information from the "meos.h" file...
start /B /wait cmd /c .\run_extractor.bat

echo Generating the "functions" file...
start /B /wait cmd /c .\run_builder.bat