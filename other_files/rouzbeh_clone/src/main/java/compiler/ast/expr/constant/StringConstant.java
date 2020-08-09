package compiler.ast.expr.constant;

import compiler.ast.type.Type;
import compiler.util.*;
import compiler.util.*;

public class StringConstant extends Constant {

    public StringConstant(String value) {
        super(value);
    }

    @Override
    public Type getResultType() {
        return Type.STRING;
    }

    @Override
    public void compile() {
        Logger.log("string constant");
        CodeWrite.mVisit.visitLdcInsn(value);
    }

    private String getStr(String str) { //TODO
        return str.substring(1, str.length() - 1);
    }

}
