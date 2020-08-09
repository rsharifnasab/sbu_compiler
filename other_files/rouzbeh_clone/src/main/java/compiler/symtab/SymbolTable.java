package compiler.symtab;

import java.util.*;
import java.util.stream.*;

import compiler.scanner.token.*;
import compiler.scanner.*;

import compiler.symtab.dscp.*;


/**
 * a tree containing keywords
 * and all variable descriptors
 *
 * key: var name
 * value: descriptor
 */
public class SymbolTable {

    private Map <String, Descriptor> table;


    public SymbolTable() {
        this.table = new HashMap<>();
    }

    /**
     * get a map of keywords
     * and set it as base table
     */
    private SymbolTable(Map<String, Descriptor> keywords){
        this.table = keywords;
    }

    public void add(AbstractDescriptor dscp){
        table.put(dscp.getName() , dscp);
    }

    public AbstractDescriptor get(String key){
        return (AbstractDescriptor) table.get(key);
    }

    public boolean contains(String key){
        return table.get(key) != null;
    }



    private final static String[] KEYWORDS = {
        "const",
        "record",
        "bool",
        "char",
        "int",
        "long",
        "float",
        "double",
        "string",
        "void",
        "true",
        "false",
        "auto",
        "function",
        "begin",
        "end",
        "if",
        "else",
        "switch",
        "of",
        "case",
        "default",
        "for",
        "foreach",
        "println",
        "input",
        "in",
        "repeat",
        "until",
        "break",
        "continue",
        "return",
        "sizeof",
        "not",
        "and",
        "or"
    };


    public static SymbolTable ofKeywords(){
        Map<String,Descriptor> KWMap = Arrays
                .stream(KEYWORDS)
                .collect(
                    Collectors.toMap(
                            kw -> kw,
                            kw -> new KeywordDescriptor()
                    )
                );
        return new SymbolTable(KWMap);
    }
}
