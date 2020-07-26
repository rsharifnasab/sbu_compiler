package compiler.scanner;

import java.io.*;

import compiler.scanner.token.*;
import compiler.scanner.symboltable.*;
import compiler.util.*;

public class Lexical {

    private final MyScanner inner;
    
    public final SymbolTable symTab;


    private Symbol lastSym = null;

    public Symbol getLastSym(){
        if(lastSym == null) throw new RuntimeException("there is nothing to read");
        return lastSym;
    }

    private void setLastSym(Symbol s){
        this.lastSym = s;
    }


    public Lexical(File inputFile) throws IOException{
        this(new FileReader(inputFile));
    }

    public Lexical(InputStream inputStream) throws IOException {
        this(new InputStreamReader(inputStream));
    }

    private Lexical(Reader r) throws IOException{
        inner = new MyScanner(r);
        symTab = new SymbolTable();
    }

    public String nextToken() throws IOException{
        Symbol s = inner.nextToken();
        setLastSym(s);

        return s.token.toString();
    }

}
