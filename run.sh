#!/bin/sh

clear
echo "deleting junk files"
find . -name "*.args" -type f -delete
find . -name "*.class" -type f -delete

echo "updating parse table"
./updateTable.sh

echo "compiling"
mvn clean package -q || exit # -Dmaven.test.skip=true || exit

echo "-- compiling a.rou--"
java -jar ./target/*s.jar a.rou out.class  || exit

echo "running compiled source"
java out || exit 
