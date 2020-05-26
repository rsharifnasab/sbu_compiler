#!/bin/sh 
clear
cd src
javac *.java
java Main  ../in.txt
rm *.class
cd ..
