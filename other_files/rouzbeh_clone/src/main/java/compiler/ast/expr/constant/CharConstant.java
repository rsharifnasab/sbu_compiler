package compiler.ast.expr.constant;

import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

public class CharConstant extends Constant {

    public CharConstant(String value) {
        super(value);
    }

    @Override
    public Type getResultType() {
        return Type.INT;
    }

    @Override
    public void compile() {
        Logger.log("character constant");
        CodeWrite.mVisit.visitVarInsn(Opcodes.BIPUSH, (int) ((char) value.charAt(0)));
    }

    private char getChar(String str) {
        String ch = str.substring(1, str.length() - 1);
        switch (ch) {
            case "\\b":
                return '\b';
            case "\\f":
                return '\f';
            case "\\n":
                return '\n';
            case "\\r":
                return '\r';
            case "\\t":
                return '\t';
            case "\\\\":
                return '\\';
            default:
                return ch.charAt(0);
        }
    }

}
