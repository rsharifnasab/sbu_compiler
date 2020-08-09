package compiler.ast.expr.constant;

import compiler.ast.type.Type;
import compiler.util.*;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

public class DoubleConstant extends Constant {

    public DoubleConstant(String value) {
        super(value);
    }

    @Override
    public Type getResultType() {
        return Type.DOUBLE;
    }

    @Override
    public void compile() {
        Logger.log("double constant");
        Double main = Double.parseDouble(value);
        if (main.equals((double) 0))
            CodeWrite.mVisit.visitInsn(Opcodes.DCONST_0);
        else if (main.equals((double) 1))
            CodeWrite.mVisit.visitInsn(Opcodes.DCONST_1);
        else
            CodeWrite.mVisit.visitLdcInsn(main);
    }

}
