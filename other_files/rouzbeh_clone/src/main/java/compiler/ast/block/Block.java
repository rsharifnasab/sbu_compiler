package compiler.ast.block;

import compiler.ast.Node;
import compiler.util.*;
import org.objectweb.asm.Label;
import compiler.symtab.SymbolTable;
import compiler.symtab.TableStack;

import java.util.*;

public class Block implements Node {

    private Label start = new Label();
    private Label end = new Label();
    private List<BlockContent> contents = new ArrayList<>();

    public Block() {
        Blocks.getInstance().add(this);
    }

    public Label getStart() {
        return start;
    }

    public Label getEnd() {
        return end;
    }

    public void addContent(BlockContent content) {
        contents.add(content);
    }

    public void init() {
        TableStack.getInstance().newSym();
    }

    public void markStart() {
        CodeWrite.mVisit.visitLabel(start);
    }

    @Override
    public void compile() {
        contents.forEach(Node::compile);
    }

    public void markEnd() {
        CodeWrite.mVisit.visitLabel(end);
        TableStack.getInstance().pop();
    }

}
