package compiler.ast.expr.unary.arithmatic.dual;

import compiler.ast.access.Access;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

import static compiler.ast.type.Type.*;

public class PrefixPlus2 extends PreOperation {

    public PrefixPlus2(Access access) {
        super(access);
    }

    @Override
    public void compile() {
        Logger.log("prefix plus plus");
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
        else
            opcode = Opcodes.IADD;
        return super.determineOp(type);
    }

}
