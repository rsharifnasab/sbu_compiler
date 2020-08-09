# SBU compiler project

in spring 99 semester, we designed a compiler for compiler design course
by _Dr Jaberi pour_  in _Shahid Beheshti university_

language name is **Roulang** (Rouzbeh + language!)

### rules & limitations

+ use [cup](http://www2.in.tum.de/projects/cup/) as scanner generator 
+ use [PGEN](https://github.com/Borjianamin98/PGen/tree/java-11) for parser generation (written by our great friends)
+ create Java byte-code (class file) with [ASM](https://asm.ow2.io/) library
+ other aspects of projects are in [phase1L syntax highlighter](./phase1.pdf), [phase2: parser generation](./phase2.pdf), [phase3 : rest of compiler](./phase3.pdf) files


### team members
+ Mehrshad Sa'adatiNia ([github link](https://github.com/mehrshad-sdtn))
+ Roozbeh Sharifnasab ([github link](https://github.com/rsharifnasab))

---------------------------

## how to use

+ install maven, Java 13+

+ open terminal in this folder and run:
```bash
  mvn clean package 
```
+ then you have a complete jar file (`target/RoulangCompiler-1.0-jar-with-dependencies.jar`)
+ to use compiler:
```bash
java -jar ./target/*s.jar path/to/in.rou /path/to/out.class
```
this will create an executable class file `out.class` from input file `in.rou`

+ to run  the class file:

  ```bash
  java out
  ```

  and for viewing whats in it:

  ```bash
  javap -p out.class
  javap -v out.class #verbose
  ```


#### notes

+ in first try to run maven, it will download dependencies, don't worry.

+ if there was problem in tests, you can skip tests (not recommended)


```bash
mvn clean package -Dmaven.test.skip=true
```
and then use the jar file


+ parser graph in `PGS` format is in parser folder, after editing that, create `npt` file in same directory named `table.npt`, it will copied to folders with `./updateTable.sh`.

+ this project is not well debugged, don't use it in production :D



--------------

## additions

+ you can syntax highlight the language with vim, when you opened file, run:       `:source syntax.vim`

+ you can import other rou files as well 



------------



### special thanks to
+ Profossor JaberiPour
+ Roozbeh paktinat
+ Alireza Asadi 
+ Amin Borjian
