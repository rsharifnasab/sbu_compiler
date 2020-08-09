package compiler.ast.expr.unary.arithmatic;

import compiler.ast.expr.Expression;
import compiler.ast.expr.unary.UnaryExpression;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

import static compiler.ast.type.Type.*;

public class BinaryNot extends UnaryExpression {

    public BinaryNot(Expression expr) {
        super(expr);
    }

    @Override
    public void compile() {
        Logger.log("binary not");
        Type resultType = getResultType();
        expr.compile();
        CodeWrite.mVisit.visitVarInsn(Opcodes.LDC, -1);
        CodeWrite.mVisit.visitInsn(determineOp(resultType));
    }

    @Override
    public int determineOp(Type type) {
        if (type == INT)
            return Opcodes.IXOR;
        else if (type == LONG)
            return Opcodes.LXOR;
        else
            Logger.error("type mismatch");
        return 0;
    }

}
