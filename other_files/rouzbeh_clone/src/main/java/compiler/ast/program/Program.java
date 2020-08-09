package compiler.ast.program;

import compiler.ast.Node;
import compiler.ast.type.Type;
import compiler.util.*;
import compiler.symtab.dscp.function.Functions;

import java.util.*;

public class Program implements Node {

    private static final Program instance = new Program();

    private List<ProgramContent> contents = new ArrayList<>();

    private Program() {
    }

    public static Program getInstance() {
        return instance;
    }

    public void addContent(ProgramContent content) {
        contents.add(content);
    }

    @Override
    public void compile() {
        Logger.log("compiling program");
        CodeWrite.initializeClass();
        CodeWrite.writePrintFunction();
        contents.forEach(ProgramContent::compile);
        checkMainExistence();
        CodeWrite.end();
    }

    private void checkMainExistence() {
        final String main_name = "start";
        if (!Functions.getInstance().contains(main_name) ||
                Functions.getInstance().get(main_name).getReturnType() != Type.VOID ||
                !Functions.getInstance().get(main_name).isCompleteDCL())
            Logger.error("program doesn't contain a "+main_name+" function");
    }

}
