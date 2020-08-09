package compiler.ast.expr.unary.arithmatic.dual;

import compiler.ast.access.Access;
import compiler.util.*;
import org.objectweb.asm.Opcodes;
import compiler.symtab.dscp.variable.GlobalVariableDescriptor;

import static compiler.util.CodeWrite.mVisit;

public class PostOperation extends DualOperation {

    PostOperation(Access access) {
        super(access);
    }

    @Override
    public void compile() {
        super.compile();
        mVisit.visitInsn(dupOp);
        mVisit.visitInsn(constOp);
        mVisit.visitInsn(opcode);
        if (descriptor instanceof GlobalVariableDescriptor)
            mVisit.visitFieldInsn(Opcodes.PUTFIELD, CodeWrite.OUTCLASS_NAME, descriptor.getName(), descriptor.getType().typeName());
        else
            mVisit.visitVarInsn(strOp, descriptor.getStackIndex());
    }

}
