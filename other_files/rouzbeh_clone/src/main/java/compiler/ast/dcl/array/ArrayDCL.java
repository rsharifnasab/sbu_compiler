package compiler.ast.dcl.array;

import compiler.ast.dcl.DCL;
import compiler.ast.dcl.variable.Variables;
import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.ast.type.TypeChecker;
import compiler.util.*;
import compiler.util.*;
import org.objectweb.asm.Opcodes;
import compiler.symtab.TableStack;
import compiler.symtab.dscp.array.ArrayDescriptor;

import java.util.Optional;

import static compiler.ast.type.Type.*;

public class ArrayDCL extends DCL {

    private Expression expr;

    public ArrayDCL(Expression expr) {
        this.expr = expr;
        descriptor = new ArrayDescriptor();
        descriptor.setConst(Variables.getInstance().isConstant());
        descriptor.setType(Type.toArray(Variables.getInstance().getType()));
    }

    @Override
    public void compile() {
        Logger.log("array declaration");
        if (!TypeChecker.isValidSwitchType(expr.getResultType()))
            Logger.error("invalid array size parameter");
        expr.compile();
        TableStack.getInstance().addVariable(descriptor);
        CodeWrite.mVisit.visitVarInsn(Opcodes.NEWARRAY, determinePrimitiveType(descriptor.getType()));
        CodeWrite.mVisit.visitVarInsn(Opcodes.ASTORE, descriptor.getStackIndex());
    }

    private int determinePrimitiveType(Type type) {
        if (type == DOUBLE_ARRAY)
            return Opcodes.T_DOUBLE;
        else if (type == FLOAT_ARRAY)
            return Opcodes.T_FLOAT;
        else if (type == LONG_ARRAY)
            return Opcodes.T_LONG;
        else if (type == INT_ARRAY)
            return Opcodes.T_INT;
        else
            Logger.log("unsupported array type");
        return 0;
    }

}
