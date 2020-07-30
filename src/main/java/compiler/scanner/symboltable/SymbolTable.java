package compiler.scanner.symboltable;

import java.util.*;

import compiler.scanner.token.*;
import compiler.scanner.*;

public class SymbolTable {


    private Map <String, Descriptor> table;
    
    private String lastValue = null;

    private final String[] KEYWORDS = {
            "const", "record", "bool", "char", "int", "long", "float", "double", "string",
            "void", "true", "false", "auto", "function", "begin", "end", "if", "else",
    "switch", "of", "case", "default", "for", "foreach", "println", "input", "in",
            "repeat", "until", "break", "continue", "return", "sizeof", "not", "and", "or"
    };

    public SymbolTable() {
        this.table = new HashMap<>();
    }


    public void add(String name , Descriptor dscp){
        table.put(name , dscp);
    }

    public int getSize(){
        return table.size();
    }


    //mehrshad added
    public Descriptor getDSCP(String key){
        return table.get(key);
    }
    public void removeEntry(String key){
        table.remove(key);
    }

    public boolean hasDefined(String key){
        return table.containsKey(key);
    }

}
