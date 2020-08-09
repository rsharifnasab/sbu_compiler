package compiler.ast.block.stmt.assignment;

import compiler.ast.access.Access;
import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

import static compiler.ast.type.Type.*;

public class DivisionAssign extends OperatorAssign {

    public DivisionAssign(Access access, Expression expr) {
        super(access, expr);
    }

    @Override
    public void compile() {
        Logger.log("division assignment");
        super.compile();
    }

    @Override
    public int determineOp(Type type) {
        if (type == DOUBLE)
            opcode = Opcodes.DDIV;
        else if (type == FLOAT)
            opcode = Opcodes.FDIV;
        else if (type == LONG)
            opcode = Opcodes.LDIV;
        else if (type == INT)
            opcode = Opcodes.IDIV;
        else
            Logger.error("type mismatch");
        return super.determineOp(type);
    }

}
