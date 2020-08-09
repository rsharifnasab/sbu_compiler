package compiler.ast.program.global;

import compiler.ast.program.Program;
import compiler.ast.program.ProgramContent;
import compiler.ast.type.Type;
import compiler.ast.type.TypeChecker;
import compiler.util.*;
import org.objectweb.asm.Opcodes;
import compiler.symtab.TableStack;
import compiler.symtab.dscp.variable.GlobalVariableDescriptor;

public class GlobalVarDCL extends ProgramContent {

    private Type type;
    private String id;

    public GlobalVarDCL(Type type, String id) {
        this.type = type;
        this.id = id;
    }

    @Override
    public void compile() {
        Logger.log("global variable declaration");
        if (!TypeChecker.isValidVariableType(type))
            Logger.error("invalid type for global variable");
        GlobalVariableDescriptor descriptor = generate();
        TableStack.getInstance().addGlobal(descriptor);
        CodeWrite.mainCLW.visitField(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, id, type.typeName(), null, null).visitEnd();
    }

    private GlobalVariableDescriptor generate() {
        GlobalVariableDescriptor descriptor = new GlobalVariableDescriptor();
        descriptor.setName(id);
        descriptor.setType(type);
        return descriptor;
    }

}
