package compiler;

import java.io.*;

import compiler.scanner.*;
import compiler.scanner.symboltable.*;

import compiler.parser.*;
import compiler.codegen.*;

import compiler.util.*;

public class Compiler {

    public static SymbolTable compileLib(String masir){

        String className = Utils.getClassNameFromFile(
                new File(masir));
        String outName = className + ".class";
        Logger.print("compiling another source file","GREEN");
        try{
            File input = new File(masir);
            File output = new File(outName);

            InputStream nptFile = Utils.getInputStreamFromResource("pt.npt");

            Lexical lexical = new Lexical(input);
            CodeGen codeGen = new CodeGen(output,lexical);
            Parser parser = new Parser(lexical,codeGen,nptFile);
            parser.parse();

            codeGen.writeEndOfClass();
            
            Logger.print("compiled another source file","GREEN");
            return lexical.symTab;

        } catch(Exception e){
            Logger.error("cannot compiler library");
        }
        return null;
    }

    public static void main(String[] args) throws IOException{
        File input = Utils.getInputFile(args);
        //input = new File("inputFiles/code.txt");

        File output = Utils.getOutputFile(args);

        InputStream nptFile = Utils.getInputStreamFromResource("pt.npt");

        Lexical lexical = new Lexical(input);
        CodeGen codeGen = new CodeGen(output,lexical);
        Parser parser = new Parser(lexical,codeGen,nptFile);
        parser.parse();

        codeGen.writeEndOfClass();




    }
}
