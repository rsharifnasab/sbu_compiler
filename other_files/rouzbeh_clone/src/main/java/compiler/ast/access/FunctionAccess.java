package compiler.ast.access;

import compiler.ast.expr.Expression;
import compiler.ast.type.Type;

import compiler.symtab.dscp.function.FunctionDescriptor;
import compiler.symtab.dscp.function.Functions;

import compiler.util.*;

import org.objectweb.asm.Opcodes;

import java.util.*;

public class FunctionAccess extends Access {

    private List<Expression> parameters;

    public FunctionAccess(List<Expression> parameters) {
        this.parameters = parameters;
    }

    @Override
    public void setDescriptor(String id) {
        this.id = id;
    }

    @Override
    public void compile() {
        Logger.log("function access compile");
        Type[] parameterTypes = parameters.stream().map(Expression::getResultType).toArray(Type[]::new);
        if (!Functions.getInstance().contains(id, parameterTypes))
            Logger.error("no function definition found");
        descriptor = Functions.getInstance().get(id, parameterTypes);
    }

    @Override
    public void push() {
        Logger.log("function access invoke");
        FunctionDescriptor descriptor = (FunctionDescriptor) this.descriptor;
        parameters.forEach(Expression::compile);
        CodeWrite.mVisit.visitMethodInsn(Opcodes.INVOKESTATIC, CodeWrite.OUTCLASS_NAME, descriptor.getName(), descriptor.getDescriptor(), false);
    }

    @Override
    public int determineOp(Type type) {
        return 0;
    }

}
