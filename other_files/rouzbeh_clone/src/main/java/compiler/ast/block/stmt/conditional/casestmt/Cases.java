package compiler.ast.block.stmt.conditional.casestmt;

import compiler.ast.block.Block;
import compiler.util.*;
import org.objectweb.asm.Label;

import java.util.*;

public class Cases {

    private static final Cases instance = new Cases();

    private Map<Integer, Block> jumpTable;
    private int max;
    private int min;

    private Cases() {
    }

    public static Cases getInstance() {
        return instance;
    }

    public void init() {
        jumpTable = new TreeMap<>();
        max = Integer.MIN_VALUE;
        min = Integer.MAX_VALUE;
    }

    public void addCase(int ca, Block block) {
        if (jumpTable.containsKey(ca))
            Logger.error("invalid case");
        jumpTable.put(ca, block);
        max = Math.max(max, ca);
        min = Math.min(min, ca);
    }

    int getMax() {
        return max;
    }

    int getMin() {
        return min;
    }

    Label[] getLabels(Label defaultLabel) {
        Label[] labels = new Label[max - min + 1];
        for (int i = min; i <= max; i++)
            labels[i - min] = jumpTable.containsKey(i) ? jumpTable.get(i).getStart() : defaultLabel;
        return labels;
    }

    List<Block> getCaseBlocks() {
        List<Block> blockList = new ArrayList<>();
        jumpTable.forEach((i, b) -> blockList.add(b));
        return blockList;
    }

}
