package compiler.ast.expr.binary.arithmatic;

import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

import static compiler.ast.type.Type.*;

public class Div extends ArithmeticBinaryExpr {

    public Div(Expression expr1, Expression expr2) {
        super(expr1, expr2);
    }

    @Override
    public void compile() {
        Logger.log("division");
        super.compile();
    }

    @Override
    public int determineOp(Type type) {
        if (type == DOUBLE)
            return Opcodes.DDIV;
        else if (type == FLOAT)
            return Opcodes.FDIV;
        else if (type == LONG)
            return Opcodes.LDIV;
        else
            return Opcodes.IDIV;
    }

}
