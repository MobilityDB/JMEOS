@echo off
setlocal enabledelayedexpansion

REM Check if the environment variable containing the path exists
if "%JMEOS_HOME%"=="" (
    echo This script requires administrative privileges to define JMEOS_HOME. Please run it as an administrator or define it.
    echo.
    echo The JMEOS_HOME environment variable is not defined. It is the absolute path of the Java project in MobilityDB-JMEOS.
    set /p JMEOS_HOME="Enter JMEOS_HOME value: "
    echo.
    echo Saving JMEOS_HOME in user environment variables...
    echo.
    setx JMEOS_HOME "!JMEOS_HOME!"
)

REM Go to project directory
cd /d "%JMEOS_HOME%"

REM Compile Java File
javac -d target/classes src/main/java/jmeos/utils/*.java src/main/java/jmeos/functions/builder/FunctionsExtractor.java
java -cp target/classes jmeos.functions.builder.FunctionsExtractor

REM Check if compilation was successful
if %errorlevel% neq 0 (
    echo Error in Java compilation.
)

REM End of script
echo End of extraction.
pause