package compiler.ast.expr.binary.arithmatic;


import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

import static compiler.ast.type.Type.*;

public class Add extends ArithmeticBinaryExpr {

    public Add(Expression expr1, Expression expr2) {
        super(expr1, expr2);
    }

    @Override
    public void compile() {
        Logger.log("add");
        super.compile();
    }

    @Override
    public int determineOp(Type type) {
        if (type == DOUBLE)
            return Opcodes.DADD;
        else if (type == FLOAT)
            return Opcodes.FADD;
        else if (type == LONG)
            return Opcodes.LADD;
        else
            return Opcodes.IADD;
    }

}
