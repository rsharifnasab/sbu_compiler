package compiler.symtab.dscp.struct;

import compiler.symtab.dscp.AbstractDescriptor;
import compiler.symtab.dscp.variable.VariableDescriptor;

import java.util.HashMap;
import java.util.Map;

public class StructureDescriptor extends AbstractDescriptor {

    private Map<String, VariableDescriptor> variables = new HashMap<>();

    public VariableDescriptor get(String name) {
        return variables.get(name);
    }

    public void addVariable(VariableDescriptor descriptor) {
        variables.put(descriptor.getName(), descriptor);
    }

    public StructureDescriptor clone() {
        StructureDescriptor newDescriptor = new StructureDescriptor();
        newDescriptor.setType(type);
        variables.forEach((key, value) -> newDescriptor.variables.put(key, value));
        return newDescriptor;
    }

}
