package compiler;

import java.io.*;

import compiler.scanner.*;
import compiler.parser.*;
import compiler.codegen.*;

import compiler.util.Utils;

public class Compiler {

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
