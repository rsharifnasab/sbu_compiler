package compiler.ast.expr.binary.logical;

import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

import static compiler.ast.type.Type.*;

public class LE extends LogicalBinaryExpr {

    public LE(Expression expr1, Expression expr2) {
        super(expr1, expr2);
    }

    @Override
    public void compile() {
        Logger.log("less than equal");
        super.compile();
    }

    @Override
    public int determineOp(Type type) {
        if (type == DOUBLE) {
            opCode = Opcodes.IFGT;
            compareCode = Opcodes.DCMPG;
        } else if (type == FLOAT) {
            opCode = Opcodes.IFGT;
            compareCode = Opcodes.FCMPG;
        } else if (type == LONG) {
            opCode = Opcodes.IFGT;
            compareCode = Opcodes.LCMP;
        } else if (type == INT)
            opCode = Opcodes.IF_ICMPGT;
        else
            Logger.error("type mismatch");
        return 0;
    }

}
