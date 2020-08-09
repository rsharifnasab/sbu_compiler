package compiler.symtab;

import java.util.*;

import compiler.util.*;
import compiler.symtab.dscp.*;
import compiler.symtab.dscp.function.*;
import compiler.ast.type.*;

import static compiler.ast.type.Type.*;

/**
 * stack  of blocks
 * showing that a variable belong tp which scope
 */
public class TableStack {
    private final static TableStack instance = new TableStack();
    public static TableStack getInstance(){
        return instance;
    }

    private final LinkedList<SymbolTable> stack;
    private final SymbolTable globals;

    private FunctionDescriptor currentFunection;

    private int stackIndex;


    private TableStack(){
        this.globals = SymbolTable.ofKeywords();
        this.stack   = new LinkedList<>();
    }


    public void pop(){
        stack.pop();
    }

    public void push(SymbolTable sym){
        stack.push(sym);
    }

    public void newSym(){
        stack.push(new SymbolTable());
    }

    public void addGlobal(AbstractDescriptor d){
        globals.add(d);
    }

    public AbstractDescriptor find(String id){
        for(int i= stack.size()-1; i>=0; i--){
            SymbolTable table = stack.get(i);
            if(table.contains(id)){
                return table.get(id);
            }
        }
        if(globals.contains(id)) return globals.get(id);

        throw new RuntimeException("variable "+id+"not found in table stack");
    }

    private SymbolTable getTop(){
        return stack.get(stack.size() -1);
    }

    public void addVariable(AbstractDescriptor d){
        SymbolTable top = getTop();
        if(top.contains(d.getName()))
                Logger.error("variable "+d.getName()+" already exists");

      d.setStackIndex(stackIndex);
      Type type = d.getType();
      stackIndex += type == LONG || type == DOUBLE ? 2 : 1;
        // TODO set stack index

        top.add(d);
    }

        public FunctionDescriptor currentFunction() {
        return currentFunection;
    }


    public void newFunction(FunctionDescriptor descriptor, boolean isMain) {
        stackIndex = isMain ? 1 : 0;
        currentFunection = descriptor;
        descriptor.getParameters().forEach(this::addVariable);
    }




}
