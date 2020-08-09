package compiler.ast.block.stmt.loop;

import compiler.ast.block.Block;
import compiler.ast.block.Blocks;
import compiler.ast.block.stmt.Statement;
import compiler.ast.type.Type;
import compiler.util.*;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import compiler.symtab.TableStack;
import compiler.symtab.dscp.array.ArrayDescriptor;
import compiler.symtab.dscp.variable.VariableDescriptor;

import static compiler.ast.type.Type.*;
import static compiler.util.CodeWrite.*;

public class Foreach extends Statement {

    private String temp;
    private String array;
    private Block body;

    private ArrayDescriptor loopArray;
    private VariableDescriptor loopTemp;
    private VariableDescriptor loopCounter;
    private int strCode;
    private int arrayLdrCode;

    public Foreach(String temp, String array, Block body) {
        this.temp = temp;
        this.array = array;
        this.body = body;
    }

    @Override
    public void compile() {
        Logger.log("foreach loop");
        initLoop();
        determineOp(Type.toSimple(loopArray.getType()));
        mVisit.visitInsn(Opcodes.ICONST_0);
        mVisit.visitVarInsn(Opcodes.ISTORE, loopCounter.getStackIndex());

        Label loopStart = new Label();
        mVisit.visitLabel(loopStart);
        mVisit.visitVarInsn(Opcodes.ALOAD, loopArray.getStackIndex());
        mVisit.visitInsn(Opcodes.ARRAYLENGTH);
        mVisit.visitVarInsn(Opcodes.ILOAD, loopCounter.getStackIndex());
        mVisit.visitJumpInsn(Opcodes.IFGE, body.getEnd());

        mVisit.visitVarInsn(Opcodes.ALOAD, loopArray.getStackIndex());
        mVisit.visitVarInsn(Opcodes.ILOAD, loopCounter.getStackIndex());
        mVisit.visitInsn(arrayLdrCode);
        mVisit.visitVarInsn(strCode, loopTemp.getStackIndex());

        body.compile();
        body.markStart();
        mVisit.visitIincInsn(loopCounter.getStackIndex(), 1);
//        mVisit.visitJumpInsn(Opcodes.GOTO, loopStart);

        body.markEnd();
        Blocks.getInstance().remove();
    }

    private void determineOp(Type type) {
        if (type == DOUBLE) {
            strCode = Opcodes.DSTORE;
            arrayLdrCode = Opcodes.DALOAD;
        } else if (type == FLOAT) {
            strCode = Opcodes.FSTORE;
            arrayLdrCode = Opcodes.FALOAD;
        } else if (type == LONG) {
            strCode = Opcodes.LSTORE;
            arrayLdrCode = Opcodes.LALOAD;
        } else if (type == INT) {
            strCode = Opcodes.ISTORE;
            arrayLdrCode = Opcodes.IALOAD;
        } else {
            strCode = Opcodes.ASTORE;
            arrayLdrCode = Opcodes.AALOAD;
        }
    }

    private void initLoop() {
        Blocks.getInstance().add(body);
        body.init();
        loopArray = (ArrayDescriptor) TableStack.getInstance().find(array);
        loopTemp = new VariableDescriptor();
        loopTemp.setName(temp);
        loopTemp.setConst(false);
        loopTemp.setType(Type.toSimple(loopArray.getType()));
        TableStack.getInstance().addVariable(loopTemp);

        loopCounter = new VariableDescriptor();
        loopCounter.setName("$t" + Blocks.getInstance().size());
        loopCounter.setConst(false);
        loopCounter.setType(INT);
        TableStack.getInstance().addVariable(loopCounter);
    }

}
