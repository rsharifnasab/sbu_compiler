package compiler.ast.expr.constant;

import compiler.ast.expr.Expression;
import compiler.ast.type.Type;

public abstract class Constant extends Expression {

    protected String value;

    public Constant(String value) {
        this.value = value;
    }

}
