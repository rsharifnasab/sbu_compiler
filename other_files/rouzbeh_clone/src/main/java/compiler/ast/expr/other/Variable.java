package compiler.ast.expr.other;

import compiler.ast.access.Access;
import compiler.ast.access.ArrayAccess;
import compiler.ast.access.StructureAccess;
import compiler.ast.access.VariableAccess;
import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.ast.type.TypeChecker;
import compiler.util.*;
import compiler.symtab.dscp.AbstractDescriptor;

public class Variable extends Expression {

    private Access access;

    public Variable(Access access) {
        this.access = access;
    }

    @Override
    public Type getResultType() {
        access.compile();
        Type type;
        if (access instanceof VariableAccess)
            type = ((AbstractDescriptor) access.getDescriptor()).getType();
        else if (access instanceof ArrayAccess)
            type = Type.toSimple(((AbstractDescriptor) access.getDescriptor()).getType());
        else
            type = ((StructureAccess) access).getStructureVar().getType();
        if (!TypeChecker.isValidVariableType(type))
            Logger.error("type mismatch");
        return type;
    }

    @Override
    public void compile() {
        Logger.log("variable push");
        access.compile();
        access.push();
    }

}
