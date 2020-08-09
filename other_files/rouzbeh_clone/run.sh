#!/bin/sh

clear
echo "deleting junk files"
find . -name "*.args" -type f -delete

echo "updating parse table"
./updateTable.sh


mvn clean package -q || exit # -Dmaven.test.skip=true 

echo "-- compiling a.rou--"
java -jar ./target/*s.jar ./src/test/resources/7.txt ./target/out.class  || exit

echo "running compiled source"
java -cp "./target" out || exit 
