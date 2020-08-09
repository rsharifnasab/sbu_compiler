package compiler.ast.block.stmt.loop;

import compiler.ast.block.Block;
import compiler.ast.block.Blocks;
import compiler.ast.block.stmt.Statement;
import compiler.ast.expr.Expression;
import compiler.util.*;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

public class Repeat extends Statement {

    private Block block;
    private Expression expr;

    public Repeat(Block block, Expression expr) {
        this.block = block;
        this.expr = expr;
    }

    @Override
    public void compile() {
        Logger.log("repeat");
        Blocks.getInstance().add(block);
        block.init();
        block.markStart();
        block.compile();
        expr.compile();
        CodeWrite.mVisit.visitJumpInsn(Opcodes.IFEQ, block.getStart());
        block.markEnd();
        Blocks.getInstance().remove();
    }

}
