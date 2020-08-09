package compiler.ast.block.stmt.assignment;

import compiler.ast.access.Access;
import compiler.ast.access.ArrayAccess;
import compiler.ast.access.StructureAccess;
import compiler.ast.access.VariableAccess;
import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;
import compiler.symtab.dscp.AbstractDescriptor;
import compiler.symtab.dscp.variable.GlobalVariableDescriptor;
import compiler.symtab.dscp.variable.VariableDescriptor;

import static compiler.util.CodeWrite.*;

public abstract class OperatorAssign extends Assignment {

    int opcode;

    OperatorAssign(Access access, Expression expr) {
        super(access, expr);
    }

    @Override
    public void compile() {
        access.compile();
        descriptor = (AbstractDescriptor) access.getDescriptor();
        checkOperation();
        if (access instanceof VariableAccess)
            variableOperatorAssign();
        else if (access instanceof ArrayAccess)
            arrayOperatorAssign();
        else
            structOperatorAssign();
    }

    private void variableOperatorAssign() {
        access.push();
        expr.compile();
        expr.doCastCompile(descriptor.getType());
        int strCode = determineOp(descriptor.getType());
        CodeWrite.mVisit.visitInsn(opcode);
        if (descriptor instanceof GlobalVariableDescriptor)
            mVisit.visitFieldInsn(Opcodes.PUTSTATIC, CodeWrite.OUTCLASS_NAME, descriptor.getName(), descriptor.getType().typeName());
        else
            mVisit.visitVarInsn(strCode, descriptor.getStackIndex());
    }

    private void arrayOperatorAssign() {
        Type type = Type.toSimple(descriptor.getType());
        arrayStoreInit();
        access.push();
        expr.compile();
        expr.doCastCompile(type);
        int strCode = determineOp(type);
        mVisit.visitInsn(opcode);
        mVisit.visitInsn(strCode);
    }

    private void structOperatorAssign() {
        VariableDescriptor structVar = ((StructureAccess) access).getStructureVar();
        mVisit.visitVarInsn(Opcodes.ALOAD, descriptor.getStackIndex());
        access.push();
        expr.compile();
        expr.doCastCompile(structVar.getType());
        determineOp(structVar.getType());
        mVisit.visitInsn(opcode);
        mVisit.visitFieldInsn(Opcodes.PUTFIELD, descriptor.getType().typeName(), structVar.getName(), structVar.getType().typeName());
    }

}
