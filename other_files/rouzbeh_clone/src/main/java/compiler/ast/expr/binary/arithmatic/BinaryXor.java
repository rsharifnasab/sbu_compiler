package compiler.ast.expr.binary.arithmatic;

import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

import static compiler.ast.type.Type.*;

public class BinaryXor extends ArithmeticBinaryExpr {

    public BinaryXor(Expression expr1, Expression expr2) {
        super(expr1, expr2);
    }

    @Override
    public void compile() {
        Logger.log("binary xor");
        super.compile();
    }

    @Override
    public int determineOp(Type type) {
        if (type == LONG)
            return Opcodes.LXOR;
        else if (type == INT)
            return Opcodes.IXOR;
        else
            Logger.error("type mismatch");
        return 0;
    }

}
