package compiler.scanner.symboltable;

import org.objectweb.asm.MethodVisitor;

public abstract class Descriptor {
    public String type;

    public Descriptor(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

