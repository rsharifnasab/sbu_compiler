package compiler.ast.expr.binary.arithmatic;

import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

import static compiler.ast.type.Type.*;

public class BinaryAnd extends ArithmeticBinaryExpr {

    public BinaryAnd(Expression expr1, Expression expr2) {
        super(expr1, expr2);
    }

    @Override
    public void compile() {
        Logger.log("binary and");
        super.compile();
    }

    @Override
    public int determineOp(Type type) {
        if (type == LONG)
            return Opcodes.LAND;
        else if (type == INT)
            return Opcodes.IAND;
        else
            Logger.error("type mismatch");
        return 0;
    }

}
