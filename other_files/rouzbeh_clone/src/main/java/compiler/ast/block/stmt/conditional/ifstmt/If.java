package compiler.ast.block.stmt.conditional.ifstmt;

import compiler.ast.block.Block;
import compiler.ast.block.stmt.Statement;
import compiler.ast.expr.Expression;
import compiler.util.*;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

public class If extends Statement {

    private Expression expr;
    private Block ifBlock;
    private Block elseBlock;

    public If(Expression expr, Block ifBlock, Block elseBlock) {
        this.expr = expr;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public void compile() {
        Logger.log("if");
        expr.compile();
        CodeWrite.mVisit.visitJumpInsn(Opcodes.IFEQ, ifBlock.getEnd());
        ifBlock.init();
        ifBlock.markStart();
        ifBlock.compile();
        if (elseBlock == null)
            ifBlock.markEnd();
        else {
            CodeWrite.mVisit.visitJumpInsn(Opcodes.GOTO, elseBlock.getEnd());
            ifBlock.markEnd();
            elseBlock.init();
            elseBlock.markStart();
            elseBlock.compile();
            elseBlock.markEnd();
        }
    }

}
