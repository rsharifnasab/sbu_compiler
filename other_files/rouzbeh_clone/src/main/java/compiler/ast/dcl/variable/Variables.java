package compiler.ast.dcl.variable;

import compiler.ast.type.Type;

public class Variables {

    private static final Variables instance = new Variables();

    private boolean constant;
    private Type type;

    private Variables() {
    }

    public static Variables getInstance() {
        return instance;
    }

    public boolean isConstant() {
        return constant;
    }

    public void setConstant(boolean constant) {
        this.constant = constant;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

}
