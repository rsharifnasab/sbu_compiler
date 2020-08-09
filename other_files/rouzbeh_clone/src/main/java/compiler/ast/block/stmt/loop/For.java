package compiler.ast.block.stmt.loop;

import compiler.ast.block.Block;
import compiler.ast.block.Blocks;
import compiler.ast.block.stmt.Statement;
import compiler.ast.block.stmt.assignment.Assignment;
import compiler.ast.expr.Expression;
import compiler.ast.expr.constant.BooleanConstant;
import compiler.util.*;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

import static compiler.util.CodeWrite.*;

public class For extends Statement {

    private Assignment init;
    private Expression condition;
    private Assignment update;
    private Block block;

    public For(Assignment init, Expression condition, Assignment update, Block block) {
        this.init = init;
        this.condition = condition;
        this.update = update;
        this.block = block;
    }

    @Override
    public void compile() {
        Logger.log("for loop");
        if (init != null)
            init.compile();
        Blocks.getInstance().add(block);
        block.init();
        Label loopStart = new Label();
        Label loopBody = new Label();
        mVisit.visitLabel(loopStart);
        if (condition != null)
            condition.compile();
        else
            new BooleanConstant("true").compile(); // TODO check 
        mVisit.visitJumpInsn(Opcodes.IFEQ, block.getEnd());
        mVisit.visitJumpInsn(Opcodes.GOTO, loopBody);

        block.markStart();
        if (update != null)
            update.compile();
        mVisit.visitJumpInsn(Opcodes.GOTO, loopStart);

        mVisit.visitLabel(loopBody);
        block.compile();
        mVisit.visitJumpInsn(Opcodes.GOTO, block.getStart());
        block.markEnd();
        Blocks.getInstance().remove();
    }

}
