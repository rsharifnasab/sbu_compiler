package compiler.ast.access;

import compiler.ast.type.Type;
import compiler.util.*;

import org.objectweb.asm.Opcodes;

import compiler.symtab.TableStack;
import compiler.symtab.dscp.struct.StructureDescriptor;
import compiler.symtab.dscp.variable.VariableDescriptor;

public class StructureAccess extends Access {

    private String varId;
    private VariableDescriptor structureVar;

    public StructureAccess(Access access) {
        this.id = access.id;
    }

    public VariableDescriptor getStructureVar() {
        return structureVar;
    }

    @Override
    public void setDescriptor(String id) {
        this.varId = id;
    }

    @Override
    public void compile() {
        Logger.log("struct access");
        descriptor = TableStack.getInstance().find(id);
        if (!(descriptor instanceof StructureDescriptor))
            Logger.error("structure type doesn't exist");
        structureVar = ((StructureDescriptor) descriptor).get(varId);
    }

    @Override
    public void push() {
        System.err.println("here");
        Logger.log("loading structure access");
        StructureDescriptor descriptor = (StructureDescriptor) this.descriptor;
        CodeWrite.mVisit.visitVarInsn(Opcodes.ALOAD, descriptor.getStackIndex());
        CodeWrite.mVisit.visitFieldInsn(Opcodes.GETFIELD, descriptor.getType().typeName(), structureVar.getName(), structureVar.getType().typeName());
    }

    @Override
    public int determineOp(Type type) {
        return 0;
    }

}
