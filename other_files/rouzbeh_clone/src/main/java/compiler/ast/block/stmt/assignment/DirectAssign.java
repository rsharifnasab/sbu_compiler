package compiler.ast.block.stmt.assignment;

import compiler.ast.access.Access;
import compiler.ast.access.ArrayAccess;
import compiler.ast.access.StructureAccess;
import compiler.ast.access.VariableAccess;
import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.util.*;
import compiler.util.*;
import org.objectweb.asm.Opcodes;
import compiler.symtab.dscp.AbstractDescriptor;
import compiler.symtab.dscp.variable.GlobalVariableDescriptor;
import compiler.symtab.dscp.variable.VariableDescriptor;

import static compiler.util.CodeWrite.*;

public class DirectAssign extends Assignment {

    public DirectAssign(Access access, Expression expr) {
        super(access, expr);
    }

    @Override
    public void compile() {
        Logger.log("direct assignment");
        access.compile();
        descriptor = (AbstractDescriptor) access.getDescriptor();
        checkOperation();
        if (access instanceof VariableAccess)
            variableDirectAssign();
        else if (access instanceof ArrayAccess)
            arrayDirectAssign();
        else
            structDirectAssign();
    }

    private void variableDirectAssign() {
        Type.inferType(descriptor, expr.getResultType());
        expr.compile();
        expr.doCastCompile(descriptor.getType());
        if (descriptor instanceof GlobalVariableDescriptor)
            mVisit.visitFieldInsn(Opcodes.PUTSTATIC, CodeWrite.OUTCLASS_NAME, descriptor.getName(), descriptor.getType().typeName());
        else
            mVisit.visitVarInsn(determineOp(descriptor.getType()), descriptor.getStackIndex());
    }

    private void arrayDirectAssign() {
        Type type = Type.toSimple(descriptor.getType());
        arrayStoreInit();
        expr.compile();
        expr.doCastCompile(type);
        mVisit.visitInsn(determineOp(type));
    }

    private void structDirectAssign() {
        VariableDescriptor structVar = ((StructureAccess) access).getStructureVar();
        mVisit.visitVarInsn(Opcodes.ALOAD, descriptor.getStackIndex());
        expr.compile();
        expr.doCastCompile(structVar.getType());
        mVisit.visitFieldInsn(Opcodes.PUTFIELD, descriptor.getType().typeName(), structVar.getName(), structVar.getType().typeName());
    }

}
