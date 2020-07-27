package compiler.scanner.symboltable;

public class VariableDescriptor extends Descriptor {
    private int address;
    public Object value;
    public VariableDescriptor(String type) {
        super(type);
    }

    public void setAddress(int address) {
        this.address = address;
    }
    public int getAddress(){
        return address;
    }


}
