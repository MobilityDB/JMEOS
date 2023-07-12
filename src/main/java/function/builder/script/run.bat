@echo off

echo Extraction des informations du fichier "meos.h".
start /B /wait cmd /c .\run_extractor.bat

echo Generation du fichier "functions".
start /B /wait cmd /c .\run_builder.bat

echo Scripts termines.
pause