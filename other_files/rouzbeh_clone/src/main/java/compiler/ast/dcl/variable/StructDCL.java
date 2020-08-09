package compiler.ast.dcl.variable;

import compiler.ast.dcl.DCL;
import compiler.util.*;
import org.objectweb.asm.Opcodes;
import compiler.symtab.TableStack;
import compiler.symtab.dscp.AbstractDescriptor;
import compiler.symtab.dscp.struct.Structures;

import static compiler.util.CodeWrite.*;

public class StructDCL extends DCL {

    StructDCL(AbstractDescriptor descriptor) {
        this.descriptor = Structures.getInstance().newDescriptor(descriptor.getType());
        this.descriptor.setName(descriptor.getName());
        this.descriptor.setConst(descriptor.isConst());
    }

    @Override
    public void compile() {
        Logger.log("struct declaration");
        TableStack.getInstance().addVariable(descriptor);
        mVisit.visitTypeInsn(Opcodes.NEW, descriptor.getType().typeName());
        mVisit.visitInsn(Opcodes.DUP);
        mVisit.visitMethodInsn(Opcodes.INVOKESPECIAL, descriptor.getType().typeName(), "<init>", "()V", false);
        mVisit.visitVarInsn(Opcodes.ASTORE, descriptor.getStackIndex());
    }

}
