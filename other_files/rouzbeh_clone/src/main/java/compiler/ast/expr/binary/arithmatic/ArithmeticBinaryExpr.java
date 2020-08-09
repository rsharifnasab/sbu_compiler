package compiler.ast.expr.binary.arithmatic;

import compiler.ast.expr.Expression;
import compiler.ast.expr.binary.BinaryExpression;
import compiler.ast.type.Type;
import compiler.util.*;

public abstract class ArithmeticBinaryExpr extends BinaryExpression {

    ArithmeticBinaryExpr(Expression expr1, Expression expr2) {
        super(expr1, expr2);
    }

    @Override
    public void compile() {
        Type resultType = getResultType();
        compileExpressions(resultType);
        CodeWrite.mVisit.visitInsn(determineOp(resultType));
    }

}
