package compiler.ast.program.structure;

import compiler.util.*;

import java.util.*;

public class Structures {

    private static final Structures instance = new Structures();

    private List<StructVarDCL> declarations;

    private Structures() {
    }

    public static Structures getInstance() {
        return instance;
    }

    public void init() {
        declarations = new ArrayList<>();
    }

    public void addDCL(StructVarDCL dcl) {
        if (declarations.contains(dcl))
            Logger.error("duplicate variable name for structure");
        declarations.add(dcl);
    }

    public StructureDCL getDCL(String typeName) {
        return new StructureDCL(typeName, declarations);
    }

}
