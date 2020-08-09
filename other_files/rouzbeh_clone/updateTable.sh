#!/bin/bash 

#git pull 
cp ./parser/table.npt src/test/resources/pt.npt
cp ./parser/table.npt src/main/resources/pt.npt
cp ./parser/table.npt src/main/java/compiler/inputFiles/pt.npt
rm ./parser/*.java || true
rm parser/pt.npt || true
echo "done"
