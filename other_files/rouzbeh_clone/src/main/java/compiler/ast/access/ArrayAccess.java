package compiler.ast.access;

import compiler.ast.expr.Expression;


import compiler.ast.type.Type;
import static compiler.ast.type.Type.*;

import compiler.util.*;

import org.objectweb.asm.Opcodes;
import compiler.symtab.TableStack;
import compiler.symtab.dscp.array.ArrayDescriptor;


public class ArrayAccess extends Access {

    private Expression index;

    public ArrayAccess(Expression index) {
        this.index = index;
    }

    public Expression getIndex() {
        return index;
    }

    @Override
    public void compile() {
        Logger.log("array access");
        descriptor = TableStack.getInstance().find(id);
        if (!(descriptor instanceof ArrayDescriptor))
            Logger.error("array not declared");
    }

    @Override
    public void push() {
        Logger.log("loading array access");
        ArrayDescriptor descriptor = (ArrayDescriptor) this.descriptor;
        CodeWrite.mVisit.visitVarInsn(Opcodes.ALOAD, descriptor.getStackIndex());
        if (index.getResultType() != INT)
            Logger.error("arrays can only be accessed using integer types");
        index.compile();
        CodeWrite.mVisit.visitInsn(determineOp(Type.toSimple(descriptor.getType())));
    }

    @Override
    public int determineOp(Type type) {
        if (type == DOUBLE)
            return Opcodes.DALOAD;
        else if (type == FLOAT)
            return Opcodes.FALOAD;
        else if (type == LONG)
            return Opcodes.LALOAD;
        else if (type == INT)
            return Opcodes.IALOAD;
        else
            Logger.error("unable to create non primitive array");
        return 0;
    }

}
