@echo off
echo Compiling Trivia Maze Application...

rem Clean bin first to prevent stale class files
if exist bin rmdir /s /q bin
mkdir bin

rem Collect all .java files and compile them
for /r src %%f in (*.java) do echo %%f >> sources.txt
javac -cp "lib/*" -d bin @sources.txt
del sources.txt

if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b %errorlevel%
)

echo Running Application...
java -cp "bin;lib/*" Main
pause
