@echo off
chcp 65001 > nul
if not exist bin mkdir bin
javac -encoding UTF-8 -cp "lib/*" -d bin src/br/com/*/*/*.java
if %errorlevel% neq 0 (pause & exit /b 1)
java -Dfile.encoding=UTF-8 -cp "bin;lib/*" br.com.starlog.main.Main
pause
