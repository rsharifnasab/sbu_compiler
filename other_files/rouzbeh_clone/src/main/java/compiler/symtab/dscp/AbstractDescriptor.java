package compiler.symtab.dscp;

import compiler.ast.type.Type;

import java.util.Objects;

public abstract class AbstractDescriptor implements Descriptor {

    protected Type type;
    protected String name;
    protected int stackIndex;
    protected boolean isConst;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConst() {
        return isConst;
    }

    public void setConst(boolean aConst) {
        isConst = aConst;
    }

    public int getStackIndex() {
        return stackIndex;
    }

    public void setStackIndex(int stackIndex) {
        this.stackIndex = stackIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractDescriptor that = (AbstractDescriptor) o;
        return name.equals(that.name);
    }

}
