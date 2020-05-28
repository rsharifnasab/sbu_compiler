#!/bin/sh 
clear
cd src

javac *.java
echo "compiled"

java Main  ../in.txt > ../out.bpk
cmp -s ../out.txt ../out.bpk && echo "test 1 passed" || echo "wrong answer on test 1"

java Main  ../in2.txt > ../out2.bpk
cmp -s ../out2.txt ../out2.bpk && echo "test 2 passed" || echo "wrong answer on test 2"

rm *.class
cd ..
rm *.bpk
