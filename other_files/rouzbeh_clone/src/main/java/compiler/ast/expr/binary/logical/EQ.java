package compiler.ast.expr.binary.logical;

import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

import static compiler.ast.type.Type.*;

public class EQ extends LogicalBinaryExpr {

    public EQ(Expression expr1, Expression expr2) {
        super(expr1, expr2);
    }

    @Override
    public void compile() {
        Logger.log("equality");
        super.compile();
    }

    @Override
    public int determineOp(Type type) {
        if (type == DOUBLE) {
            opCode = Opcodes.IFNE;
            compareCode = Opcodes.DCMPG;
        } else if (type == FLOAT) {
            opCode = Opcodes.IFNE;
            compareCode = Opcodes.FCMPG;
        } else if (type == LONG) {
            opCode = Opcodes.IFNE;
            compareCode = Opcodes.LCMP;
        } else if (type == INT)
            opCode = Opcodes.IF_ICMPNE;
        else
            Logger.error("type mismatch");
        return 0;
    }

}
