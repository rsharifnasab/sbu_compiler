package compiler.ast.block.stmt.loop;

import compiler.ast.block.Blocks;
import compiler.ast.block.stmt.Statement;
import compiler.util.*;
import compiler.util.*;
import org.objectweb.asm.Opcodes;

public class Break extends Statement {

    @Override
    public void compile() {
        Logger.log("break");
        try {
            CodeWrite.mVisit.visitJumpInsn(Opcodes.GOTO, Blocks.getInstance().getCurrent().getEnd());
        } catch (Exception e) {
            Logger.error("break operation should only be used in loops");
        }
    }

}
