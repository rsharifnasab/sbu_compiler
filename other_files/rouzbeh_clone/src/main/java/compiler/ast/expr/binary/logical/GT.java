package compiler.ast.expr.binary.logical;

import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

import static compiler.ast.type.Type.*;

public class GT extends LogicalBinaryExpr {

    public GT(Expression expr1, Expression expr2) {
        super(expr1, expr2);
    }

    @Override
    public void compile() {
        Logger.log("grater than");
        super.compile();
    }

    @Override
    public int determineOp(Type type) {
        if (type == DOUBLE) {
            opCode = Opcodes.IFLE;
            compareCode = Opcodes.DCMPG;
        } else if (type == FLOAT) {
            opCode = Opcodes.IFLE;
            compareCode = Opcodes.FCMPG;
        } else if (type == LONG) {
            opCode = Opcodes.IFLE;
            compareCode = Opcodes.LCMP;
        } else if (type == INT)
            opCode = Opcodes.IF_ICMPLE;
        else
            Logger.error("type mismatch");
        return 0;
    }

}
