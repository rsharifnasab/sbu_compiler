package compiler.ast.expr.unary.arithmatic;

import compiler.ast.expr.Expression;
import compiler.ast.expr.unary.UnaryExpression;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

import static compiler.ast.type.Type.*;

public class Uminus extends UnaryExpression {

    public Uminus(Expression expr) {
        super(expr);
    }

    @Override
    public void compile() {
        Logger.log("unary minus");
        Type resultType = getResultType();
        expr.compile();
        CodeWrite.mVisit.visitInsn(determineOp(resultType));
    }

    @Override
    public int determineOp(Type type) {
        if (type == DOUBLE)
            return Opcodes.DNEG;
        else if (type == FLOAT)
            return Opcodes.FNEG;
        else if (type == LONG)
            return Opcodes.LNEG;
        else
            return Opcodes.INEG;
    }

}
