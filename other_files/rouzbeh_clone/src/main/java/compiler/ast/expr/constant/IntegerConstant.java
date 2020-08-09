package compiler.ast.expr.constant;

import compiler.ast.type.Type;
import compiler.util.*;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

public class IntegerConstant extends Constant {

    public IntegerConstant(String value) {
        super(value);
    }

    @Override
    public Type getResultType() {
        return Type.INT;
    }

    @Override
    public void compile() {
        Logger.log("integer constant");
        int main = Integer.parseInt(value);
        switch (main) {
            case -1:
                CodeWrite.mVisit.visitInsn(Opcodes.ICONST_M1);
                break;
            case 0:
                CodeWrite.mVisit.visitInsn(Opcodes.ICONST_0);
                break;
            case 1:
                CodeWrite.mVisit.visitInsn(Opcodes.ICONST_1);
                break;
            case 2:
                CodeWrite.mVisit.visitInsn(Opcodes.ICONST_2);
                break;
            case 3:
                CodeWrite.mVisit.visitInsn(Opcodes.ICONST_3);
                break;
            case 4:
                CodeWrite.mVisit.visitInsn(Opcodes.ICONST_4);
                break;
            case 5:
                CodeWrite.mVisit.visitInsn(Opcodes.ICONST_5);
                break;
            default: {
                if (main > Byte.MIN_VALUE && main < Byte.MAX_VALUE)
                    CodeWrite.mVisit.visitVarInsn(Opcodes.BIPUSH, main);
                else if (main > Short.MIN_VALUE && main < Short.MAX_VALUE)
                    CodeWrite.mVisit.visitVarInsn(Opcodes.SIPUSH, main);
                else
                    CodeWrite.mVisit.visitLdcInsn(value);
            }
        }
    }

}
