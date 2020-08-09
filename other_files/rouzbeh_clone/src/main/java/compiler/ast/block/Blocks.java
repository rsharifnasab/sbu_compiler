package compiler.ast.block;

import java.util.Stack;

public class Blocks {

    private static final Blocks instance = new Blocks();

    private final Stack<Block> BLOCKS = new Stack<>();

    private Blocks() {
    }

    public static Blocks getInstance() {
        return instance;
    }

    public void add(Block block) {
        BLOCKS.push(block);
    }

    public void remove() {
        BLOCKS.pop();
    }

    public int size() {
        return BLOCKS.size();
    }

    public Block getCurrent() {
        return BLOCKS.peek();
    }

}
