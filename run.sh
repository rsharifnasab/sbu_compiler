#!/bin/sh

clear
echo "deleting junk files"
find . -name "*.args" -type f -delete

echo "updating parse table"
./updateTable.sh

echo "compiling"
mvn clean package -q -Dmaven.test.skip=true || exit

echo "-- compiling a.rou--"
java -jar ./target/*s.jar r.rou out.class  || exit

echo "running compiled source"
java out || exit 
