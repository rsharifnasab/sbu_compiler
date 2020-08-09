package compiler.ast.dcl;

import compiler.ast.block.BlockContent;
import compiler.ast.dcl.variable.VariableDCL;
import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

import static compiler.ast.type.Type.*;

public class CompleteDCL extends BlockContent {

    private DCL dcl;
    private Expression expr;

    public CompleteDCL(DCL dcl, Expression expr) {
        this.dcl = dcl;
        this.expr = expr;
    }

    @Override
    public void compile() {
        Logger.log("complete local variable declaration");
        dcl.compile();
        if (expr == null)
            return;
        if (!(dcl instanceof VariableDCL))
            Logger.error("invalid initialization");
        else {
            Type type = dcl.getDescriptor().getType();
            expr.compile();
            expr.doCastCompile(type);
            CodeWrite.mVisit.visitVarInsn(determineOp(type), dcl.getDescriptor().getStackIndex());
        }
    }

    private int determineOp(Type type) {
        if (type == DOUBLE)
            return Opcodes.DSTORE;
        else if (type == FLOAT)
            return Opcodes.FSTORE;
        else if (type == LONG)
            return Opcodes.LSTORE;
        else if (type == INT)
            return Opcodes.ISTORE;
        else
            return Opcodes.ASTORE;
    }

}
