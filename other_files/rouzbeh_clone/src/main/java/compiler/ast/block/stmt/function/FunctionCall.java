package compiler.ast.block.stmt.function;

import compiler.ast.access.FunctionAccess;
import compiler.ast.block.BlockContent;
import compiler.ast.type.Type;
import compiler.util.*;
import compiler.util.*;
import org.objectweb.asm.Opcodes;
import compiler.symtab.dscp.function.FunctionDescriptor;

import static compiler.ast.type.Type.*;

public class FunctionCall extends BlockContent {

    private FunctionAccess access;

    public FunctionCall(FunctionAccess access) {
        this.access = access;
    }

    @Override
    public void compile() {
        Logger.log("function call statement");
        access.compile();
        access.push();
        Type returnType = ((FunctionDescriptor) access.getDescriptor()).getReturnType();
        if (returnType != VOID)
            CodeWrite.mVisit.visitInsn(!(returnType == DOUBLE || returnType == LONG) ? Opcodes.POP : Opcodes.POP2);
    }

}
