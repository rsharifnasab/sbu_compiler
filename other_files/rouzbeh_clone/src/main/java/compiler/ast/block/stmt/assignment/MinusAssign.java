package compiler.ast.block.stmt.assignment;

import compiler.ast.access.Access;
import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

import static compiler.ast.type.Type.*;

public class MinusAssign extends OperatorAssign {

    public MinusAssign(Access access, Expression expr) {
        super(access, expr);
    }

    @Override
    public void compile() {
        Logger.log("minus assignment");
        super.compile();
    }

    @Override
    public int determineOp(Type type) {
        if (type == DOUBLE)
            opcode = Opcodes.DSUB;
        else if (type == FLOAT)
            opcode = Opcodes.FSUB;
        else if (type == LONG)
            opcode = Opcodes.LSUB;
        else if (type == INT)
            opcode = Opcodes.ISUB;
        else
            Logger.error("type mismatch");
        return super.determineOp(type);
    }

}
