package compiler.ast.block.stmt.function;

import compiler.ast.block.BlockContent;
import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.util.*;
import compiler.util.*;
import org.objectweb.asm.Opcodes;
import compiler.symtab.TableStack;

import static compiler.ast.type.Type.*;

public class Return extends BlockContent {

    private Expression expr;

    public Return(Expression expr) {
        this.expr = expr;
    }

    @Override
    public void compile() {
        Logger.log("return statement");
        expr.compile();
        CodeWrite.mVisit.visitInsn(determineOp(expr.getResultType()));
    }

    private int determineOp(Type type) {
        Type currentFuncReturnType = TableStack.getInstance().currentFunction().getReturnType();
        if (type == DOUBLE && currentFuncReturnType == DOUBLE)
            return Opcodes.DRETURN;
        else if (type == FLOAT && currentFuncReturnType == FLOAT)
            return Opcodes.FRETURN;
        else if (type == LONG && currentFuncReturnType == LONG)
            return Opcodes.LRETURN;
        else if (type == INT && (currentFuncReturnType == INT || currentFuncReturnType == CHAR || currentFuncReturnType == BOOL))
            return Opcodes.IRETURN;
        else
            Logger.error("invalid function return type");
        return 0;
    }

}
