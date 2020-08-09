package compiler.ast.expr.binary.logical;

import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

import static compiler.ast.type.Type.*;

public class LT extends LogicalBinaryExpr {

    public LT(Expression expr1, Expression expr2) {
        super(expr1, expr2);
    }

    @Override
    public void compile() {
        Logger.log("less than");
        super.compile();
    }

    @Override
    public int determineOp(Type type) {
        if (type == DOUBLE) {
            opCode = Opcodes.IFGE;
            compareCode = Opcodes.DCMPG;
        } else if (type == FLOAT) {
            opCode = Opcodes.IFGE;
            compareCode = Opcodes.FCMPG;
        } else if (type == LONG) {
            opCode = Opcodes.IFGE;
            compareCode = Opcodes.LCMP;
        } else if (type == INT)
            opCode = Opcodes.IF_ICMPGE;
        else
            Logger.error("type mismatch");
        return 0;
    }

}
