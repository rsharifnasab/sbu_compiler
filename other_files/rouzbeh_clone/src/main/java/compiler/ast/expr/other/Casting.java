package compiler.ast.expr.other;

import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.ast.type.TypeChecker;
import compiler.util.*;

public class Casting extends Expression {

    private Expression expr;
    private Type type;

    public Casting(Type type, Expression expr) {
        this.expr = expr;
        this.type = type;
    }

    @Override
    public Type getResultType() {
        return TypeChecker.unaryExprTypeCheck(type);
    }

    @Override
    public void compile() {
        Logger.log("type casting");
        expr.compile();
        expr.doCastCompile(getResultType());
    }

}
