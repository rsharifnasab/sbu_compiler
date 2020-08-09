package compiler.symtab.dscp.function;

import compiler.ast.type.Type;
import compiler.symtab.dscp.AbstractDescriptor;
import compiler.symtab.dscp.Descriptor;
import compiler.symtab.dscp.variable.VariableDescriptor;

import java.util.*;

public class FunctionDescriptor implements Descriptor {

    private String name;
    private Type returnType;
    private Type[] parameterTypes;
    private List<VariableDescriptor> parameters;
    private boolean completeDCL;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public Type[] getParameterTypes() {
        return parameterTypes;
    }

    void setParameterTypes(Type... parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public List<VariableDescriptor> getParameters() {
        return parameters;
    }

    public void setParameters(List<VariableDescriptor> parameters) {
        this.parameters = parameters;
        parameterTypes = parameters.stream().map(AbstractDescriptor::getType).toArray(Type[]::new);
    }

    public boolean isCompleteDCL() {
        return completeDCL;
    }

    public void setCompleteDCL(boolean completeDCL) {
        this.completeDCL = completeDCL;
    }

    public String getDescriptor() {
        StringBuilder descriptor = new StringBuilder("(");
        Arrays.stream(parameterTypes).forEach(type -> descriptor.append(type.typeName()));
        descriptor.append(")");
        descriptor.append(returnType.typeName());
        return descriptor.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionDescriptor that = (FunctionDescriptor) o;
        return name.equals(that.name) &&
                Arrays.equals(parameterTypes, that.parameterTypes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name);
        result = 31 * result + Arrays.hashCode(parameterTypes);
        return result;
    }

}
