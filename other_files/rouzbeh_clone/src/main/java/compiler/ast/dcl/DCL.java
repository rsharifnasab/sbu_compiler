package compiler.ast.dcl;

import compiler.ast.block.BlockContent;
import compiler.symtab.dscp.AbstractDescriptor;

public abstract class DCL extends BlockContent {

    protected AbstractDescriptor descriptor;

    public void setId(String id) {
        descriptor.setName(id);
    }

    public AbstractDescriptor getDescriptor() {
        return descriptor;
    }

}
