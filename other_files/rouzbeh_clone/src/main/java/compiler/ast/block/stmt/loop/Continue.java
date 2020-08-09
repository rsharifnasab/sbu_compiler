package compiler.ast.block.stmt.loop;

import compiler.ast.block.Blocks;
import compiler.ast.block.stmt.Statement;
import compiler.util.*;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

public class Continue extends Statement {

    @Override
    public void compile() {
        Logger.log("continue");
        try {
            CodeWrite.mVisit.visitJumpInsn(Opcodes.GOTO, Blocks.getInstance().getCurrent().getStart());
        } catch (Exception e) {
            Logger.error("continue operation should only be used in loops");
        }
    }

}
