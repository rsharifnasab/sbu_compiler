package compiler.scanner;

import java.io.*;

import compiler.scanner.token.*;
import compiler.util.*;

public class Lexical {

    private final MyScanner inner;
    
    private Symbol beforeLastSym = null;
    private Symbol lastSym = null;

    public Symbol getLastSym(){
        if(lastSym == null) throw new RuntimeException("there is nothing to read");
        //return beforeLastSym;
        return lastSym;
    }

    private void setLastSym(Symbol s){
        this.beforeLastSym = lastSym;
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
    }

    public String nextToken() throws IOException{
        Symbol s = inner.nextToken();
        setLastSym(s);

        return s.token.toString();
    }

}
