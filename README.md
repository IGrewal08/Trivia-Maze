# Trivia-Maze
Trivia Maze is a MVC Java based project developed by a team of three following Agile development practices tracked using YouTrack and version-control on GitHub.

Team members:
- Anwar Noor
- Inderdeep Grewal
- Nicolas Cortes

- SRS Document: https://docs.google.com/document/d/1_U6ARgyAsv9eKRTok4VLZbCh8CAvT_4PmWOdwl6V0Ps/edit?usp=sharing

Issues Encountered:
- None.


### Running Tests:
(On windows swap `:` for `;`)
#### Compile (with .jar files in lib):
- javac -cp "lib/*:src" -d bin src/model/*.java src/tests/model/<TEST_NAME>.java
#### Run:
- java -jar lib/junit-platform-console-standalone-1.11.4.jar --class-path bin --select-class=tests.model.<TEST_NAME>

### For Controller:
#### Compile:
(On windows swap `:` for `;`)
- javac -cp "lib/*:src" -d bin src/model/*.java src/controller/*.java src/view/*.java src/tests/controller/GameControllerTest.java
#### Run:
- java -jar lib/junit-platform-console-standalone-1.11.4.jar --class-path bin --select-class=tests.controller.GameControllerTest

#### Run All Tests
java -jar lib/junit-platform-console-standalone-1.11.4.jar --class-path bin --select-package=tests





