package compiler.ast.expr.binary;

import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.ast.type.TypeChecker;

public abstract class BinaryExpression extends Expression {

    protected Expression expr1;
    protected Expression expr2;

    public BinaryExpression(Expression expr1, Expression expr2) {
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    public abstract int determineOp(Type type);

    @Override
    public Type getResultType() {
        return TypeChecker.binaryExprTypeCheck(expr1.getResultType(), expr2.getResultType());
    }

    protected void compileExpressions(Type resultType) {
        expr1.compile();
        expr1.doCastCompile(resultType);
        expr2.compile();
        expr2.doCastCompile(resultType);
    }

}
