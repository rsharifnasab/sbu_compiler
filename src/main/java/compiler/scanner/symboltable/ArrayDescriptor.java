package compiler.scanner.symboltable;

public class ArrayDescriptor extends Descriptor{
    private int address;
    public ArrayDescriptor(String type) {
        super(type);
    }
    public void setAddress(int address) {
        this.address = address;
    }
    public int getAddress(){
        return address;
    }
}
