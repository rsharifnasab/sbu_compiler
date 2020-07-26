package compiler.scanner.symboltable;

public class VariableDescriptor extends Descriptor {
    Object value;
    public VariableDescriptor(String type) {
        super(type);
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
