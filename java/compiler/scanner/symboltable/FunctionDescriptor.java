package compiler.scanner.symboltable;

import compiler.scanner.Symbol;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FunctionDescriptor extends Descriptor{
   public MethodVisitor mv;
   public List<String> argumentTypes;
   public SymbolTable innerTable;

   public FunctionDescriptor(String returnType , MethodVisitor mv) {
       super(returnType);
       this.mv = mv;
       this.argumentTypes = new ArrayList<>();
       this.innerTable = new SymbolTable();
   }
}
