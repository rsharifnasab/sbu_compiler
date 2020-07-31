package compiler.codegen;

import compiler.scanner.symboltable.Descriptor;
import compiler.scanner.symboltable.FunctionDescriptor;
import compiler.scanner.symboltable.SymbolTable;
import compiler.scanner.symboltable.VariableDescriptor;

import org.objectweb.asm.*;
import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Stack;

import compiler.util.*;
import compiler.scanner.*;
import org.objectweb.asm.tree.analysis.Value;


import static org.objectweb.asm.Opcodes.*;

public class CodeGen {

  public final String OUTCLASS_NAME;
  public final File OUTPUT_FILE;
  public static final String SUPER_CLASS = "java/lang/Object";

  public ClassWriter mainCLW;
  public ClassWriter structCLW;
  public MethodVisitor mVisit;
  public Deque<String> semanticStack;
  public String currentFunc;
  public Deque<String> helpStack;

  private final Lexical lexical;

  public CodeGen(File output, Lexical lexical) {
    OUTPUT_FILE = output;
    OUTCLASS_NAME = Utils.getClassNameFromFile(output);
    this.lexical = lexical;
    this.semanticStack = new ArrayDeque<>();
    this.helpStack = new ArrayDeque<>();
    initializeClass();

  }

  private String getLastValue(){
    return lexical.getLastSym().content;
  }


  private void initializeClass(){

    Logger.log("Initializing code generator");
    mainCLW = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    mainCLW.visit(V1_8, ACC_PUBLIC, OUTCLASS_NAME, null, SUPER_CLASS, null);

    mVisit = mainCLW.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
    mVisit.visitCode();
    mVisit.visitVarInsn(ALOAD, 0);
    mVisit.visitMethodInsn(INVOKESPECIAL, SUPER_CLASS, "<init>", "()V", false);
    mVisit.visitInsn(RETURN);
    mVisit.visitMaxs(1, 1);
    mVisit.visitEnd();


  }



  public void writeEndOfClass(){
    Logger.log("writing generated code into output file");
    mainCLW.visitEnd();

    writeAndCheckClass(mainCLW.toByteArray(), OUTPUT_FILE, OUTCLASS_NAME );
    Logger.close();

  }

  public void writeRecordClass(String record_out){
    Logger.log("writing record to file");
    File record_out_file = new File(
        OUTPUT_FILE.getParentFile().getName(),
        record_out+".class"
    );
    writeAndCheckClass(structCLW.toByteArray(), record_out_file, record_out);
  }

  private static void writeAndCheckClass(byte[] b,File location, String className){
    finalCheckClass(b,className);
    try(OutputStream out = new FileOutputStream(location)){
      out.write(b);
    } catch(IOException e){
      e.printStackTrace();
    }
  }


  @SuppressWarnings("rawtypes")
  private static Class finalCheckClass(byte[] b, String className) {
    // Override defineClass (as it is protected) and define the class.
    Class clazz = null;
    try {
      ClassLoader loader = ClassLoader.getSystemClassLoader();
      Class cls = Class.forName("java.lang.ClassLoader");
      java.lang.reflect.Method method =
      cls.getDeclaredMethod(
      "defineClass",
      new Class[] { String.class, byte[].class, int.class, int.class });

      // Protected method invocation.
      method.setAccessible(true);
      try {
        Object[] args =
        new Object[] { className, b, Integer.valueOf(0), Integer.valueOf(b.length)};
        clazz = (Class) method.invoke(loader, args);
        clazz.getConstructor().newInstance();
      } finally {
        method.setAccessible(false);
      }
    } catch (Throwable e) {
      e.printStackTrace();
      Logger.error("compilation failed");
    }
    Logger.print("check doneeeeeeee","green");

    return clazz;
  }




  public int getOp(String type) {

    switch (type) {
      case "double":
      return  Opcodes.DSTORE;
      case "float":
      return Opcodes.FSTORE;
      case "long":
      return Opcodes.LSTORE;
      case "int":
      return Opcodes.ISTORE;
      default:
      return Opcodes.ASTORE;
    }
  }

  public int returnOp(String type) {

    switch (type) {
      case "double":
      return  DRETURN;
      case "float":
      return FRETURN;
      case "long":
      return LRETURN;
      case "int":
      return IRETURN;
      case "void":
      return RETURN;
      default:
      return ARETURN;
    }
  }
  public int loadOp(String type) {

    switch (type) {
      case "double":
      return  DLOAD;
      case "float":
      return FLOAD;
      case "long":
      return LLOAD;
      case "int":
      return ILOAD;
      default:
      return ALOAD;
    }
  }
  //------------------------------
  public int addOp(String type) {

    switch (type) {
      case "double":
      return  DADD;
      case "float":
      return FADD;
      case "long":
      return LADD;
      case "int":
      return IADD;
      default:
      return 0;
    }
  }
  //-----------------------------
  public int subOp(String type){
    switch (type) {
      case "double":
      return  DSUB;
      case "float":
      return FSUB;
      case "long":
      return LSUB;
      case "int":
      return ISUB;
      default:
      return 0;
    }
  }

  public int divOp(String type){
    switch (type) {
      case "double":
      return  DDIV;
      case "float":
      return FDIV;
      case "long":
      return LDIV;
      case "int":
      return IDIV;
      default:
      return 0;
    }
  }

  public int mulOp(String type){
    switch (type) {
      case "double":
      return  DMUL;
      case "float":
      return FMUL;
      case "long":
      return LMUL;
      case "int":
      return IMUL;
      default:
      return 0;
    }
  }
  //--------------------------------------------
  public int compareOp(String comp){
    switch (comp) {
      case "==":
      return IF_ICMPNE;
      case "!=":
      return IF_ICMPEQ;
      case "<=":
      return IF_ICMPGT;
      case ">=":
      return IF_ICMPLT;
      case "<":
      return IF_ICMPGE;
      case ">":
      return IF_ICMPLE;
      default:
      return 0;

    }
  }


  //------------------------------------------------
  public void typeLdcInsn(MethodVisitor mv , String type , String literal){
    switch (type) {
      case "double":
      mv.visitLdcInsn(Double.parseDouble(literal));
      break;
      case "float":
      mv.visitLdcInsn(Float.parseFloat(literal));
      break;
      case "long":
      mv.visitLdcInsn(Long.parseLong(literal));
      break;
      case "int":
      mv.visitLdcInsn(Integer.parseInt(literal));
      break;
      case "byte":
      mv.visitLdcInsn(Byte.parseByte(literal));
      break;
      case "bool":
      if(Integer.parseInt(literal) == 0)
      mv.visitInsn(ICONST_0);
      else if(Integer.parseInt(literal) == 1)
      mv.visitInsn(ICONST_1);
      break;
      default:
      mv.visitLdcInsn(literal);
      break;
    }
  }





  ///------codegen main logic
  public void doSemantic(String semantic){

    TypeDescMap mapper = new TypeDescMap();

    semantic = semantic.replace("@","");
    var st = lexical.symTab;

    String lastValue = lexical.getLastSym().content;
    var lastToken = lexical.getLastSym().token;
    Logger.print("sem:"+semantic+", last sym:"+lexical.getLastSym(),"yellow");

    switch(semantic){

      case "make_function_dscp":{
        String retType = lastToken.toString();
        if (retType.contains("_")){
          retType = retType.split("_")[1];
        }
        retType = retType.toLowerCase();

        FunctionDescriptor functionDscp = new FunctionDescriptor(retType, null);
        st.add("$temp" , functionDscp);
        break;
      }


      //----------------------------------
      case "set_function_name":{
        String funcName = lastValue;
        Descriptor temp = st.getDSCP("$temp");
        st.removeEntry("$temp");
        st.add(funcName , temp);
        currentFunc = funcName;


        break;

      }
      //------------------------------------------------------------
      case "add_args":{
        var argToken = lastToken.toString().split("_")[1].toLowerCase();
        semanticStack.push(argToken);
        break;

      }
      ///--------------------------------------------------------------
      case "add_arg_dscp":{
        Descriptor argDscp = new VariableDescriptor(semanticStack.peek());
        var symtab = ((FunctionDescriptor)st.getDSCP(currentFunc)).innerTable;
        symtab.add(lastValue, argDscp);
        break;


      }
      //-------------------------------------------------------------------
      case "complete_args":{
        String args = "";
        var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
        if(currentFunc.equals("start")){
          dscp.mv = mainCLW.visitMethod(ACC_PUBLIC|ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        }

        else{
          while (!semanticStack.isEmpty()){
            var type = semanticStack.pop();
            dscp.argumentTypes.add((String) type);
            args = mapper.map.get(type) + args;
          }
          args = "("+args+")";
          String signature = args + mapper.map.get(dscp.getType());
          System.out.println(signature);
          dscp.mv = mainCLW.visitMethod(ACC_PUBLIC|ACC_STATIC, currentFunc, signature, null, null);
        }
        break;


      }
      //-----------------------------------------------------
      case "visit_body":{

        var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
        dscp.mv.visitCode();

        break;
      }

      //----------------------------------------------------
      case "declare":{
        var type = lastToken.toString().split("_")[1].toLowerCase();
        semanticStack.push(type);



      }break;

      //-----------------------------------
      case "push_lit":{
        semanticStack.push(lastValue);
        helpStack.push(lastToken.toString().split("_")[0].toLowerCase());


        break;
      }

      //-----------------------------------------
      case "complete_assign":{
        var value = semanticStack.pop();
        var name  = semanticStack.pop();
        var type  = semanticStack.pop();

        var functionDscp = (FunctionDescriptor)st.getDSCP(currentFunc);
        VariableDescriptor varDscp = new VariableDescriptor(type);
        int address = functionDscp.innerTable.getSize();
        varDscp.setAddress(address);

        functionDscp.innerTable.add(name, varDscp);

        // 1 extra slots for double
        if(type.equals("double")||type.equals("long")){
          functionDscp.innerTable.add(null,null);
        }

        if(helpStack.peek().equals("IDENTIFIER")){ // if the right side is id , load it's value
        helpStack.pop();
        var adr = ((VariableDescriptor)functionDscp.innerTable.getDSCP(value)).getAddress();
        functionDscp.mv.visitVarInsn(loadOp(type), adr);
      }
      else typeLdcInsn(functionDscp.mv, type, value);//-push for every type

      functionDscp.mv.visitVarInsn(getOp(type), address);


      break;
    }

    //------------------------------------------------------------------------
    case "push_dcl_name":{
      semanticStack.push(lastValue);

    }
    break;
    //-----------------------------------------------------------------------
    case "simple_assign":{
      var functionDscp = (FunctionDescriptor)st.getDSCP(currentFunc);
      var literal = semanticStack.pop();
      var name    = semanticStack.pop();

      if(!functionDscp.innerTable.hasDefined(name)){
        System.err.println("compile error: variable "+name+" is not defined at function "+currentFunc+"()");
        System.exit(404);//terminate compilation
      }
      else{

        var varDscp = (VariableDescriptor)functionDscp.innerTable.getDSCP(name);
        varDscp.value = literal;
        var type = varDscp.type;

        //---loads for every type
        typeLdcInsn(functionDscp.mv,type,literal);
        functionDscp.mv.visitVarInsn(getOp(type), varDscp.getAddress());



      }
      break;


    }


    //--------------------------------------------------------------------
    case "push_id":{
      semanticStack.push(lastValue);
      System.out.print("");
      helpStack.push(lastToken.toString());
      break;

    }


    //----------------------------------------------
    case "print":{

      var literal = lastToken.toString().toLowerCase();
      String param;
      String type = null;

      if(literal.equals("icv"))
      param = "I";
      else{
        type = literal.trim().split("_")[0];
        param = mapper.map.get(type);
      }


      String argType = "("+param+")";
      var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
      dscp.mv.visitCode();
      dscp.mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
      //pushes the data on the stack
      typeLdcInsn(dscp.mv, type, lastValue);

      //print call
      dscp.mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", argType+"V", false);



      break;
    }
    //------------------------------------------------------------------
    case "print_value":{
      var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
      var id = lastValue;
      var varDscp = ((VariableDescriptor)dscp.innerTable.getDSCP(id));
      var adr = varDscp.getAddress();
      var type = varDscp.getType();


      var argType = "(" + mapper.map.get(type) + ")";
      dscp.mv.visitCode();
      dscp.mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
      //pushes the data on the stack
      dscp.mv.visitVarInsn(loadOp(type), adr);

      //print call
      dscp.mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", argType+"V", false);


      break;
    }
    //------------------------------------------------------------------------
    case "new_scanner":{
      var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
      dscp.mv.visitTypeInsn(NEW, "java/util/Scanner");
      dscp.mv.visitInsn(DUP);
      dscp.mv.visitFieldInsn(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");
      dscp.mv.visitMethodInsn(INVOKESPECIAL, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V");





      break;
    }
    ///-----------------------------------------------------------------------
    case "get_input":{
      var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);

      var callType = semanticStack.pop();  //e.g. double
      var temp = callType;
      var methodType = mapper.map.get(callType); // e.g. D
      var firstLetter = callType.substring(0,1).toUpperCase(); // d->D
      callType = callType.substring(1); // e.g. ouble
      String methodCall;


      if(temp.equals("string"))
      methodCall = "next";
      else
      methodCall = "next" + firstLetter + callType;


      dscp.mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/Scanner", methodCall, "()"+methodType);

      dscp.mv.visitVarInsn(getOp(temp), dscp.innerTable.getSize());



      break;

    }

    case "get_string":{
      var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
      dscp.mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/Scanner", "next", "()"+"Ljava/lang/String;");
      dscp.mv.visitVarInsn(ASTORE, dscp.innerTable.getSize());

      break;
    }
    ///------------------------------------------------------------------------
    case "input_assign":{
      var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
      var varName = semanticStack.pop();
      var varType = semanticStack.pop();
      VariableDescriptor varDscp = new VariableDescriptor(varType);
      varDscp.setAddress(dscp.innerTable.getSize());
      dscp.innerTable.add(varName, varDscp);

      //1 extra slot for long and double
      if(varType.equals("double") || varType.equals("long")){
        dscp.innerTable.add(null, null);
      }
      helpStack.clear();


      break;


    }
    //-----------------------------------------------------------------------
    case "add":{
      var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
      String addType = null;
      //-

      for (int i = 0; i < 2; i++) {

        if (helpStack.peek().equalsIgnoreCase("IDENTIFIER")) {
          helpStack.pop();
          var name = semanticStack.pop();

          var varDscp = ((VariableDescriptor) dscp.innerTable.getDSCP(name));

          //search type and address in innerTable
          var address = varDscp.getAddress();
          var type = varDscp.getType();
          dscp.mv.visitVarInsn(loadOp(type), address);
          addType = type;


        } else {
          var literalType = helpStack.pop();
          var val = semanticStack.pop();
          typeLdcInsn(dscp.mv, literalType, val);
          // System.out.println(literalType+"= "+val);
          addType = literalType;
        }
      }


      dscp.mv.visitInsn(addOp(addType));
      var adr = dscp.innerTable.getSize();
      dscp.mv.visitVarInsn(getOp(addType), adr);

      VariableDescriptor tempDscp = new VariableDescriptor(addType);
      tempDscp.setAddress(adr);
      dscp.innerTable.add("system_temp", tempDscp); //temporary value

      //push type into helper
      helpStack.push("IDENTIFIER");
      semanticStack.push("system_temp");





      break;
    }
    ///----------------------------------------------------------------------
    case "sub":{
      var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
      String addType = null;
      //-

      for (int i = 0; i < 2; i++) {

        if (helpStack.peek().equalsIgnoreCase("IDENTIFIER")) {
          helpStack.pop();
          var name = semanticStack.pop();

          var varDscp = ((VariableDescriptor) dscp.innerTable.getDSCP(name));

          //search type and address in innerTable
          var address = varDscp.getAddress();
          var type = varDscp.getType();
          dscp.mv.visitVarInsn(loadOp(type), address);
          addType = type;


        } else {
          var literalType = helpStack.pop();
          var val = semanticStack.pop();
          typeLdcInsn(dscp.mv, literalType, val);
          // System.out.println(literalType+"= "+val);
          addType = literalType;
        }
      }


      dscp.mv.visitInsn(subOp(addType));
      var adr = dscp.innerTable.getSize();
      dscp.mv.visitVarInsn(getOp(addType), adr);

      VariableDescriptor tempDscp = new VariableDescriptor(addType);
      tempDscp.setAddress(adr);
      dscp.innerTable.add("system_temp", tempDscp); //temporary value

      //push type into helper
      helpStack.push("IDENTIFIER");
      semanticStack.push("system_temp");


      break;
    }
    //-----------------------------------------------------------------------
    case "mult":{

      var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
      String addType = null;
      //-

      for (int i = 0; i < 2; i++) {

        if (helpStack.peek().equalsIgnoreCase("IDENTIFIER")) {
          helpStack.pop();
          var name = semanticStack.pop();

          var varDscp = ((VariableDescriptor) dscp.innerTable.getDSCP(name));

          //search type and address in innerTable
          var address = varDscp.getAddress();
          var type = varDscp.getType();
          dscp.mv.visitVarInsn(loadOp(type), address);
          addType = type;


        } else {
          var literalType = helpStack.pop();
          var val = semanticStack.pop();
          typeLdcInsn(dscp.mv, literalType, val);
          // System.out.println(literalType+"= "+val);
          addType = literalType;
        }
      }


      dscp.mv.visitInsn(mulOp(addType));
      var adr = dscp.innerTable.getSize();
      dscp.mv.visitVarInsn(getOp(addType), adr);

      VariableDescriptor tempDscp = new VariableDescriptor(addType);
      tempDscp.setAddress(adr);
      dscp.innerTable.add("system_temp", tempDscp); //temporary value

      //push type into helper
      helpStack.push("IDENTIFIER");
      semanticStack.push("system_temp");


      break;
    }
    //-----------------------------------------------------------------------
    case "div" :{

      var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
      String addType = null;
      //-

      for (int i = 0; i < 2; i++) {

        if (helpStack.peek().equalsIgnoreCase("IDENTIFIER")) {
          helpStack.pop();
          var name = semanticStack.pop();

          var varDscp = ((VariableDescriptor) dscp.innerTable.getDSCP(name));

          //search type and address in innerTable
          var address = varDscp.getAddress();
          var type = varDscp.getType();
          dscp.mv.visitVarInsn(loadOp(type), address);
          addType = type;


        } else {
          var literalType = helpStack.pop();
          var val = semanticStack.pop();
          typeLdcInsn(dscp.mv, literalType, val);
          // System.out.println(literalType+"= "+val);
          addType = literalType;
        }
      }


      dscp.mv.visitInsn(divOp(addType));
      var adr = dscp.innerTable.getSize();
      dscp.mv.visitVarInsn(getOp(addType), adr);

      VariableDescriptor tempDscp = new VariableDescriptor(addType);
      tempDscp.setAddress(adr);
      dscp.innerTable.add("system_temp", tempDscp); //temporary value

      //push type into helper
      helpStack.push("IDENTIFIER");
      semanticStack.push("system_temp");


      break;

    }
    //-----------------------------------------------------------------------
    case "call":{
      var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);

      String argTypes = "";
      var id = semanticStack.pollLast(); // function name
      helpStack.pollLast();//pop name type from help stack

      while(semanticStack.size() != 0){

        var literal = semanticStack.pollLast();
        var temp = helpStack.pollLast();
        typeLdcInsn(dscp.mv, temp, literal);
        argTypes += mapper.map.get(temp);
      }
      var arguments = "(" + argTypes + ")";


      var retType = st.getDSCP(id).getType();
      retType = mapper.map.get(retType);//function return type


      dscp.mv.visitMethodInsn(INVOKESTATIC, OUTCLASS_NAME, id, arguments + retType, false);
      break;
    }
    //-----------------------------------------------------------------------
    case "compare_push":{
      System.out.println(lastValue);

    }

    ///-------------------------------------------------------------------------
    case "end_function":{
      var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
      while (!semanticStack.isEmpty()) semanticStack.pop();

      dscp.mv.visitInsn(returnOp(dscp.type));
      dscp.mv.visitMaxs(dscp.innerTable.getSize() + 2, 1);
      dscp.mv.visitEnd();


      break;
    }






    case "done":
    System.out.println("ok done");
    break;

  }

}


}