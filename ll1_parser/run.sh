#!/bin/sh 
tests="./tests"
temp="./tmp"
out="./out"
mkdir $temp
mkdir $out 

clear
javac -d $out src/*.java && echo "compiled"

for i in 1 2 
do 
    java -cp $out Main "$tests/in$i.txt" > "$temp/out"
    cmp  "$tests/out$i.txt" "$temp/out" && echo "test $i passed" || echo "wrong answer on test $i"
done 

rm -r $temp
rm -r $out
