#!/bin/bash 

sudo archlinux-java set java-8-openjdk
echo "set to java8"
javac A.java  || exit
java -classpath ".:asm-7.1.jar:asm-util-7.1.jar" org.objectweb.asm.util.ASMifier A.class > ADump.java || exit 
nvim ADump.java -c ':norm ggVG='
sudo archlinux-java set java-14-openjdk
echo "set back to java 14"
#rm A.class 
#rm ADump.java
