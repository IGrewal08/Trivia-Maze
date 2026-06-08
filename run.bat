@echo off
echo Compiling Trivia Maze Application...
javac -cp "lib/*" -sourcepath src -d bin src/Main.java

if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b %errorlevel%
)

echo Running Application...
java -cp "bin;lib/*" Main
pause
