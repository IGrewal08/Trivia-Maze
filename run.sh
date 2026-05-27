echo Compiling Trivia Maze Application...
wrapper_javac() {
  javac -cp "lib/*" -sourcepath src -d bin src/Main.java  
}
wrapper_javac

if [ $? -ne 0 ]; then
    echo "[ERROR] Compilation failed!"
    exit 1
fi

echo Running Application...
java -cp "bin:lib/*" Main