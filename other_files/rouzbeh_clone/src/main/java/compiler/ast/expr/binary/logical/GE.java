package compiler.ast.expr.binary.logical;

import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

import static compiler.ast.type.Type.*;

public class GE extends LogicalBinaryExpr {

    public GE(Expression expr1, Expression expr2) {
        super(expr1, expr2);
    }

    @Override
    public void compile() {
        Logger.log("grater than equal");
        super.compile();
    }

    @Override
    public int determineOp(Type type) {
        if (type == DOUBLE) {
            opCode = Opcodes.IFLT;
            compareCode = Opcodes.DCMPG;
        } else if (type == FLOAT) {
            opCode = Opcodes.IFLT;
            compareCode = Opcodes.FCMPG;
        } else if (type == LONG) {
            opCode = Opcodes.IFLT;
            compareCode = Opcodes.LCMP;
        } else if (type == INT)
            opCode = Opcodes.IF_ICMPLT;
        else
            Logger.error("type mismatch");
        return 0;
    }

}
