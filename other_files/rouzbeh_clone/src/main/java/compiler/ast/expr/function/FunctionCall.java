package compiler.ast.expr.function;

import compiler.ast.access.FunctionAccess;
import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.util.*;
import compiler.symtab.dscp.function.FunctionDescriptor;

public class FunctionCall extends Expression {

    private FunctionAccess access;

    public FunctionCall(FunctionAccess access) {
        this.access = access;
    }

    @Override
    public Type getResultType() {
        access.compile();
        return ((FunctionDescriptor) access.getDescriptor()).getReturnType();
    }

    @Override
    public void compile() {
        Logger.log("expression function call");
        access.compile();
        access.push();
    }

}
