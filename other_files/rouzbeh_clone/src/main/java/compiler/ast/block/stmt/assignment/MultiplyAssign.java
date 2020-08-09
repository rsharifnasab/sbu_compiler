package compiler.ast.block.stmt.assignment;

import compiler.ast.access.Access;
import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

import static compiler.ast.type.Type.*;

public class MultiplyAssign extends OperatorAssign {

    public MultiplyAssign(Access access, Expression expr) {
        super(access, expr);
    }

    @Override
    public void compile() {
        Logger.log("multiply assignment");
        super.compile();
    }

    @Override
    public int determineOp(Type type) {
        if (type == DOUBLE)
            opcode = Opcodes.DMUL;
        else if (type == FLOAT)
            opcode = Opcodes.FMUL;
        else if (type == LONG)
            opcode = Opcodes.LMUL;
        else if (type == INT)
            opcode = Opcodes.IMUL;
        else
            Logger.error("type mismatch");
        return super.determineOp(type);
    }

}
