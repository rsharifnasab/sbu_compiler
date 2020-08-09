package compiler.ast.dcl.variable;

import compiler.ast.dcl.DCL;
import compiler.ast.type.TypeChecker;
import compiler.util.*;
import compiler.symtab.TableStack;
import compiler.symtab.dscp.variable.VariableDescriptor;

public class VariableDCL extends DCL {

    public VariableDCL() {
        descriptor = new VariableDescriptor();
        descriptor.setConst(Variables.getInstance().isConstant());
        descriptor.setType(Variables.getInstance().getType());
    }

    @Override
    public void compile() {
        if (TypeChecker.isValidVariableType(descriptor.getType())) {
            Logger.log("variable declaration");
            TableStack.getInstance().addVariable(descriptor);
        } else {
            new StructDCL(descriptor).compile();
        }
    }

}
