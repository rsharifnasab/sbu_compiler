package compiler.ast.block.stmt.assignment;

import compiler.ast.access.Access;
import compiler.ast.access.ArrayAccess;
import compiler.ast.access.StructureAccess;
import compiler.ast.access.VariableAccess;
import compiler.ast.block.BlockContent;
import compiler.ast.expr.Expression;
import compiler.ast.type.Type;
import compiler.util.*;
import compiler.util.*;
import org.objectweb.asm.Opcodes;
import compiler.symtab.dscp.AbstractDescriptor;

import static compiler.ast.type.Type.*;

public abstract class Assignment extends BlockContent {

    protected Access access;
    protected Expression expr;
    protected AbstractDescriptor descriptor;

    public Assignment(Access access, Expression expr) {
        this.access = access;
        this.expr = expr;
    }

    void checkOperation() {
        if (descriptor.isConst() ||
                ((access instanceof StructureAccess) && (((StructureAccess) access)).getStructureVar().isConst()))
            Logger.error("constant variables can't be changed");
    }

    public int determineOp(Type type) {
        boolean varAccess = access instanceof VariableAccess;
        if (type == DOUBLE)
            return varAccess ? Opcodes.DSTORE : Opcodes.DASTORE;
        else if (type == FLOAT)
            return varAccess ? Opcodes.FSTORE : Opcodes.FASTORE;
        else if (type == LONG)
            return varAccess ? Opcodes.LSTORE : Opcodes.LASTORE;
        else if (type == INT)
            return varAccess ? Opcodes.ISTORE : Opcodes.IASTORE;
        else
            return varAccess ? Opcodes.ASTORE : Opcodes.AASTORE;
    }

    void arrayStoreInit() {
        CodeWrite.mVisit.visitVarInsn(Opcodes.ALOAD, descriptor.getStackIndex());
        Expression indexExpr = ((ArrayAccess) access).getIndex();
        if (indexExpr.getResultType() != INT)
            Logger.error("arrays can only be accessed using integer values");
        indexExpr.compile();
    }

}
