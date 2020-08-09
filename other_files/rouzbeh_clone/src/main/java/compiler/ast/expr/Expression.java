package compiler.ast.expr;

import compiler.ast.Node;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

import static compiler.ast.type.Type.*;
import static compiler.util.CodeWrite.mVisit;

public abstract class Expression implements Node {

    public abstract Type getResultType();

    public void doCastCompile(Type resultType) {
        Type type = getResultType();
        if (type == resultType)
            return;
        if (type == DOUBLE) {
            if (resultType == FLOAT)
                mVisit.visitInsn(Opcodes.D2F);
            else if (resultType == LONG)
                mVisit.visitInsn(Opcodes.D2L);
            else if (resultType == INT)
                mVisit.visitInsn(Opcodes.D2I);
            else
                Logger.error("type mismatch");
        } else if (type == FLOAT) {
            if (resultType == DOUBLE)
                mVisit.visitInsn(Opcodes.F2D);
            else if (resultType == LONG)
                mVisit.visitInsn(Opcodes.F2L);
            else if (resultType == INT)
                mVisit.visitInsn(Opcodes.F2I);
            else
                Logger.error("type mismatch");
        } else if (type == LONG) {
            if (resultType == DOUBLE)
                mVisit.visitInsn(Opcodes.L2D);
            else if (resultType == FLOAT)
                mVisit.visitInsn(Opcodes.L2F);
            else if (resultType == INT)
                mVisit.visitInsn(Opcodes.L2I);
            else
                Logger.error("type mismatch");
        } else if (type == INT) {
            if (resultType == DOUBLE)
                mVisit.visitInsn(Opcodes.I2D);
            else if (resultType == FLOAT)
                mVisit.visitInsn(Opcodes.I2F);
            else if (resultType == LONG)
                mVisit.visitInsn(Opcodes.I2L);
            else
                Logger.error("type mismatch");
        } else
            Logger.error("type mismatch");
    }

}
