package compiler.ast.access;

import compiler.ast.Node;
import compiler.ast.type.Type;
import compiler.symtab.dscp.Descriptor;

public abstract class Access implements Node {

    String id;
    Descriptor descriptor;

    /**
     * Access nodes have two level compilations. One is accessing the descriptor in compile method
     * and one is pushing the value in this method. Note that this function should always be called
     * after compile method invoke.
     */
    public abstract void push();

    public abstract int determineOp(Type type);

    public void setDescriptor(String id) {
        this.id = id;
    }

    public Descriptor getDescriptor() {
        return descriptor;
    }

}
