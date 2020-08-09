package compiler.ast.access;

import compiler.ast.expr.Expression;
import compiler.ast.type.Type;

import java.util.*;


public class FunctionAccessData {

    private static final FunctionAccessData instance = new FunctionAccessData();

    private List<Expression> parameters = new ArrayList<>();

    private FunctionAccessData() {
    }

    public static FunctionAccessData getInstance() {
        return instance;
    }

    public void init() {
        parameters = new ArrayList<>();
    }

    public void addParameter(Expression expr) {
        parameters.add(expr);
    }

    public List<Expression> getParameters() {
        return parameters;
    }

}
