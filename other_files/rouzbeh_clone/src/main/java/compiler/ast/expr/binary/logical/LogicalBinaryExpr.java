package compiler.ast.expr.binary.logical;

import compiler.ast.expr.Expression;
import compiler.ast.expr.binary.BinaryExpression;
import compiler.ast.type.Type;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

import static compiler.util.CodeWrite.*;

public abstract class LogicalBinaryExpr extends BinaryExpression {

    int opCode;
    int compareCode;

    LogicalBinaryExpr(Expression expr1, Expression expr2) {
        super(expr1, expr2);
    }

    @Override
    public void compile() {
        Type resultType = getResultType();
        compileExpressions(resultType);
        Label l1 = new Label();
        Label l2 = new Label();
        determineOp(resultType);
        if (resultType != Type.INT)
            mVisit.visitInsn(compareCode);
        mVisit.visitJumpInsn(opCode, l1);
        mVisit.visitInsn(Opcodes.ICONST_1);
        mVisit.visitJumpInsn(Opcodes.GOTO, l2);
        mVisit.visitLabel(l1);
        mVisit.visitInsn(Opcodes.ICONST_0);
        mVisit.visitLabel(l2);
    }

    /**
     * In the subclasses determineOp method doesn't return the codes but set the ones needed for this class
     */

}
