package compiler.ast.program.function;

import compiler.ast.block.Block;
import compiler.ast.program.ProgramContent;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Opcodes;
import compiler.symtab.TableStack;
import compiler.symtab.dscp.function.FunctionDescriptor;
import compiler.symtab.dscp.function.Functions;

import static compiler.util.CodeWrite.*;

public class FunctionDCL extends ProgramContent {

    private Type type;
    private String id;
    private Block block;
    private FunctionDescriptor descriptor;

    public FunctionDCL(Type type, String id, Block block) {
        this.type = type;
        this.id = id;
        this.block = block;
        generate();
    }

    @Override
    public void compile() {
        Logger.log("function declaration");
        if (checkOperation())
            Functions.getInstance().addFunction(descriptor);
        else {
            if (!Functions.getInstance().contains(descriptor.getName(), descriptor.getParameterTypes()))
                Functions.getInstance().addFunction(descriptor);
            else
                descriptor = Functions.getInstance().get(descriptor.getName(), descriptor.getParameterTypes());
            descriptor.setCompleteDCL(true);
            writeFunction();
        }
    }

    private void generate() {
        descriptor = new FunctionDescriptor();
        descriptor.setName(id);
        descriptor.setReturnType(type);
        descriptor.setParameters(FunctionArguments.getInstance().getArguments());
    }

    private boolean checkOperation() {
        if (Functions.getInstance().contains(descriptor.getName(), descriptor.getParameterTypes())) {
            descriptor = Functions.getInstance().get(descriptor.getName(), descriptor.getParameterTypes());
            if (descriptor.isCompleteDCL() || block == null)
                Logger.error("invalid function declaration");
            return false;
        } else
            return block == null;
    }

    private void writeFunction() {
        boolean isMain =
          descriptor.getName().equals("start") &&
          descriptor.getReturnType() == Type.VOID &&
          descriptor.getParameterTypes().length == 0;

        if(isMain) Logger.log("start ro didim!");

        mVisit = mainCLW.visitMethod(
                  Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC,
                  isMain? "main" : descriptor.getName(),
                  isMain? "([Ljava/lang/String;)V" : descriptor.getDescriptor(),
                  null,
                  null
                );
        mVisit.visitCode();
        block.init();
        TableStack.getInstance().newFunction(descriptor, isMain);
        block.markStart();
        block.compile();
        block.markEnd();
        if (descriptor.getReturnType() == Type.VOID || isMain)
            mVisit.visitInsn(Opcodes.RETURN);
        mVisit.visitMaxs(1, 1);
        mVisit.visitEnd();
    }

}
