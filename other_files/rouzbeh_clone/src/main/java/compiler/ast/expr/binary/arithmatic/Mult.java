package compiler.ast.expr.binary.arithmatic;

import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

import static compiler.ast.type.Type.*;

public class Mult extends ArithmeticBinaryExpr {

    public Mult(Expression expr1, Expression expr2) {
        super(expr1, expr2);
    }

    @Override
    public void compile() {
        Logger.log("multiply");
        super.compile();
    }

    @Override
    public int determineOp(Type type) {
        if (type == DOUBLE)
            return Opcodes.DMUL;
        else if (type == FLOAT)
            return Opcodes.FMUL;
        else if (type == LONG)
            return Opcodes.LMUL;
        else
            return Opcodes.IMUL;
    }

}
