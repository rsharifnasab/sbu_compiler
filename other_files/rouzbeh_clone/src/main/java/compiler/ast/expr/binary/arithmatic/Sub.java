package compiler.ast.expr.binary.arithmatic;

import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

import static compiler.ast.type.Type.*;

public class Sub extends ArithmeticBinaryExpr {

    public Sub(Expression expr1, Expression expr2) {
        super(expr1, expr2);
    }

    @Override
    public void compile() {
        Logger.log("subtraction");
        super.compile();
    }

    @Override
    public int determineOp(Type type) {
        if (type == DOUBLE)
            return Opcodes.DSUB;
        else if (type == FLOAT)
            return Opcodes.FSUB;
        else if (type == LONG)
            return Opcodes.LSUB;
        else
            return Opcodes.ISUB;
    }

}
