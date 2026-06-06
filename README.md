# Trivia-Maze
Trivia Maze is a MVC Java based project developed by a team of three following Agile development practices tracked using YouTrack and version-control on GitHub.

Team members:
- Anwar Noor
- Inderdeep Grewal
- Nicolas Cortes

- SRS Document: https://docs.google.com/document/d/1_U6ARgyAsv9eKRTok4VLZbCh8CAvT_4PmWOdwl6V0Ps/edit?usp=sharing

Issues Encountered:
- None.


### Running Individual Tests:
(On windows swap `:` for `;`)
#### Run:
- `java -cp "lib/junit-platform-console-standalone-1.11.4.jar:lib/sqlite-jdbc-3.47.1.0.jar:bin" org.junit.platform.console.ConsoleLauncher --select-class=tests.<package>.<FileName>`
    - Ex: `java -cp "lib/junit-platform-console-standalone-1.11.4.jar:lib/sqlite-jdbc-3.47.1.0.jar:bin" org.junit.platform.console.ConsoleLauncher --select-class=tests.model.QuestionFactoryTest`

### For Controller:
(On windows swap `:` for `;`)
#### Run:
- `java -cp "lib/junit-platform-console-standalone-1.11.4.jar:lib/sqlite-jdbc-3.47.1.0.jar:bin" org.junit.platform.console.ConsoleLauncher --select-package=tests.controller`

### For Database:
(On windows swap `:` for `;`)
#### Run: 
- `java -cp "lib/junit-platform-console-standalone-1.11.4.jar:lib/sqlite-jdbc-3.47.1.0.jar:bin" org.junit.platform.console.ConsoleLauncher --select-package=tests.db`

### Run All Tests
(On windows swap `:` for `;`)
- `java -cp "lib/junit-platform-console-standalone-1.11.4.jar:lib/sqlite-jdbc-3.47.1.0.jar:bin" org.junit.platform.console.ConsoleLauncher --select-package=tests`






