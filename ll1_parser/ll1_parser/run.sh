#!/bin/sh 
clear
cd src
javac *.java
time java Main  ../in.txt
rm *.class
cd ..
