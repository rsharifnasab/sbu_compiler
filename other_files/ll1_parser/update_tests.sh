#!/bin/sh 
tests="./tests"
temp="./tmp"
out="./out"
mkdir $temp
mkdir $out 

clear
javac -d $out src/*.java && echo "compiled"

for i in 1 2 3
do 
    java -cp $out Main "$tests/in$i.txt" > "$tests/out$i.txt" 
done 

rm -r $temp
rm -r $out
