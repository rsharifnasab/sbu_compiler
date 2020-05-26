#!/bin/sh 
cd src
javac *.java
java Main  ../in.txt
rm *.class
cd ..
