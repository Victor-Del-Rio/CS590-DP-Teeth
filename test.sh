#!/bin/bash

echo "are we here"

javac Teeth.java

echo "Main-Class: Teeth" > output.txt

jar cfm Teeth.jar output.txt Teeth.class

java -jar Teeth.jar input.txt
