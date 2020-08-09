package compiler.ast.block.stmt.assignment;

import compiler.ast.access.Access;
import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

import static compiler.ast.type.Type.*;

public class PlusAssign extends OperatorAssign {

    public PlusAssign(Access access, Expression expr) {
        super(access, expr);
    }

    @Override
    public void compile() {
        Logger.log("plus assignment");
        super.compile();
    }

    @Override
    public int determineOp(Type type) {
        if (type == DOUBLE)
            opcode = Opcodes.DADD;
        else if (type == FLOAT)
            opcode = Opcodes.FADD;
        else if (type == LONG)
            opcode = Opcodes.LADD;
        else if (type == INT)
            opcode = Opcodes.IADD;
        else
            Logger.error("type mismatch");
        return super.determineOp(type);
    }

}
