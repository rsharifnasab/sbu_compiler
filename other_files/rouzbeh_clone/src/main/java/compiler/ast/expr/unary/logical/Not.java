package compiler.ast.expr.unary.logical;

import compiler.ast.expr.Expression;
import compiler.ast.expr.unary.UnaryExpression;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

import static compiler.util.CodeWrite.mVisit;

public class Not extends UnaryExpression {

    public Not(Expression expr) {
        super(expr);
    }

    @Override
    public void compile() {
        Logger.log("not");
        Type resultType = getResultType();
        expr.compile();
        Label l1 = new Label();
        Label l2 = new Label();
        mVisit.visitJumpInsn(determineOp(resultType), l1);
        mVisit.visitInsn(Opcodes.ICONST_0);
        mVisit.visitJumpInsn(Opcodes.GOTO, l2);
        mVisit.visitLabel(l1);
        mVisit.visitInsn(Opcodes.ICONST_1);
        mVisit.visitLabel(l2);
    }

    @Override
    public int determineOp(Type type) {
        if (type == Type.INT)
            return Opcodes.IFEQ;
        else
            Logger.error("type mismatch");
        return 0;
    }

}
