package compiler.ast.expr.constant;

import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

public class BooleanConstant extends Constant {

    public BooleanConstant(String value) {
        super(value);
    }

    @Override
    public Type getResultType() {
        return Type.INT;
    }

    @Override
    public void compile() {
        Logger.log("boolean constant");
        CodeWrite.mVisit.visitInsn(
            ( value.equals("true") )? //TODO 
             Opcodes.ICONST_1 : //true
             Opcodes.ICONST_0 // false
            );
    }

}
