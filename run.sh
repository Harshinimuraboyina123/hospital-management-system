#!/bin/bash

# Compile
echo "Compiling project..."
javac -d out -cp "lib/mysql-connector-java-8.0.28.jar" $(find src -name "*.java")

# Run the main class
echo "Running Hospital Management System..."
java -cp "out:lib/mysql-connector-java-8.0.28.jar" system.Main
