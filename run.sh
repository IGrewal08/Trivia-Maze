# run.sh
echo "Compiling Trivia Maze Application..."

rm -rf bin/*
mkdir -p bin

javac -cp "lib/*" -d bin $(find src -name "*.java")

if [ $? -ne 0 ]; then
    echo "[ERROR] Compilation failed!"
    exit 1
fi

echo "Running Application..."
java -cp "bin:lib/*" Main