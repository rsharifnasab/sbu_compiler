package compiler.ast.program.structure;

import compiler.ast.program.ProgramContent;
import compiler.ast.type.Type;

import compiler.util.*;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import compiler.symtab.dscp.struct.StructureDescriptor;
import compiler.symtab.dscp.struct.Structures;

import java.util.*;

import static compiler.util.CodeWrite.*;

public class StructureDCL extends ProgramContent {

    private String typeName;
    private List<StructVarDCL> declarations;

    public StructureDCL(String typeName, List<StructVarDCL> declarations) {
        this.typeName = typeName;
        this.declarations = declarations;
    }

    @Override
    public void compile() {
        Logger.log("compiling struct");
        createDescriptor();
        structCLW = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        structCLW.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, typeName, null, CodeWrite.SUPER_CLASS, null);
        declarations.forEach(StructVarDCL::compile);

        mVisit = structCLW.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mVisit.visitCode();
        mVisit.visitVarInsn(Opcodes.ALOAD, 0);
        mVisit.visitMethodInsn(Opcodes.INVOKESPECIAL, CodeWrite.SUPER_CLASS, "<init>", "()V", false);
        declarations.forEach(dcl -> dcl.init(typeName));
        mVisit.visitInsn(Opcodes.RETURN);
        mVisit.visitMaxs(1, 1);
        mVisit.visitEnd();

        structCLW.visitEnd();
        CodeWrite.writeStructureClassCode(typeName);
    }

    private void createDescriptor() {
        StructureDescriptor descriptor = new StructureDescriptor();
        if (Structures.getInstance().typeExists(typeName))
            Logger.error("structure type already exists");
        descriptor.setType(new Type(typeName));
        declarations.stream().map(StructVarDCL::getDescriptor).forEach(descriptor::addVariable);
        Structures.getInstance().addStructure(descriptor);
    }

}
