package compiler;

import java.io.*;

import compiler.scanner.*;
import compiler.parser.*;
import compiler.codegen.*;

import compiler.ast.program.*;
import compiler.util.*;

/**
 * main class of out compiler
 * get input file and output file from args
 *
 * run parser and lexer to create AST
 *
 * then create code with ASM library
 * and write it to file as bytearray
 */
public class Compiler {

    public static void main(String[] args) throws IOException{

        File input = Utils.getInputFile(args);
        //input = new File("inputFiles/code.txt");

        File output = Utils.getOutputFile(args);


        InputStream nptFile = Utils.getInputStreamFromResource("pt.npt");


        /**
         * scanner wrapper
         * get input source and get ready to scanning
         */
        Lexical lexical = new Lexical(input);

        /**
         * parser will call this to
         * generate AST
         * it will save output to Program static class
         */
        CodeGen codeGen = new CodeGen(lexical);

        Parser parser = new Parser(lexical,codeGen,nptFile,/*debug:*/false);
        parser.parse();



        CodeWrite.setOutput(output);

        /**
        * recursively compile program parts
        */
        Program.getInstance().compile();
    }
}
