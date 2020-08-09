package compiler.ast.program.structure;

import compiler.ast.expr.constant.Constant;
import compiler.ast.program.ProgramContent;
import compiler.util.*;
import org.objectweb.asm.Opcodes;
import compiler.symtab.dscp.variable.VariableDescriptor;

public class StructVarDCL extends ProgramContent {

    private Constant constant;
    private VariableDescriptor descriptor;

    public StructVarDCL(Constant constant) {
        this.constant = constant;
        descriptor = new VariableDescriptor();
    }

    public VariableDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public void compile() {
        Logger.log("declaring struct field");
        CodeWrite.structCLW.visitField
                (Opcodes.ACC_PUBLIC, descriptor.getName(), descriptor.getType().typeName(), null, null).visitEnd();
    }

    /**
     * structures should be initialized in class constructor so there will be two code generations for them.
     */
    public void init(String typeName) {
        Logger.log("initializing struct variable value in constructor");
        if (constant != null) {
            CodeWrite.mVisit.visitVarInsn(Opcodes.ALOAD, 0);
            constant.compile();
            constant.doCastCompile(descriptor.getType());
            CodeWrite.mVisit.visitFieldInsn(Opcodes.PUTFIELD, typeName, descriptor.getName(), descriptor.getType().typeName());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StructVarDCL that = (StructVarDCL) o;
        return descriptor.equals(that.descriptor);
    }

}
