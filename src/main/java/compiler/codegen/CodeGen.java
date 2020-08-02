package compiler.codegen;

import compiler.scanner.symboltable.*;

import jdk.swing.interop.SwingInterOpUtils;
import org.objectweb.asm.*;
import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

import compiler.util.*;
import compiler.scanner.*;


import static org.objectweb.asm.Opcodes.*;

public class CodeGen {

  public final String OUTCLASS_NAME;
  public final File OUTPUT_FILE;
  public static final String SUPER_CLASS = "java/lang/Object";

  public ClassWriter mainCLW;
  public ClassWriter structCLW;
  public MethodVisitor mVisit;
  public Deque<String> semanticStack;
  public Stack<Label> labelStack;
  public Deque<String> helpStack;
  public Label currentLabel;
  public ClassWriter currentRecord;

  private final Lexical lexical;


  public String currentFunc;


  public CodeGen(File output, Lexical lexical) {
    OUTPUT_FILE = output;
    OUTCLASS_NAME = Utils.getClassNameFromFile(output);
    this.lexical = lexical;
    this.semanticStack = new ArrayDeque<>();
    this.helpStack = new ArrayDeque<>();
    this.labelStack = new Stack<>();
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
    writeAndCheckClass(currentRecord.toByteArray(), record_out_file, record_out);
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
  ///-------------------------------------------------------------------------------------------------------------------
  public int makeArrayOp(String type){
    switch (type) {
      case "double":
        return  T_DOUBLE;
      case "int":
        return T_INT;
      case "long":
        return T_LONG;
      case "float":
        return T_FLOAT;
      case "bool":
        return T_BOOLEAN;
      case "char":
        return T_CHAR;
      case "byte":
       return T_BYTE;
      case "short":
        return T_SHORT;
      default:
        return 0;
    }

  }
  //--
  public int setElementOp(String type){
    switch (type) {
      case "double":
        return  DASTORE;
      case "int":
        return IASTORE;
      case "long":
        return LASTORE;
      case "float":
        return FASTORE;
      case "bool":
      case "byte":
        return BASTORE;
      case "char":
        return CASTORE;
      case "short":
        return SASTORE;
      default:
        return AASTORE;
    }

  }
  //---
  public int getElementOp(String type){
    switch (type) {
      case "double":
        return  DALOAD;
      case "int":
        return IALOAD;
      case "long":
        return LALOAD;
      case "float":
        return FALOAD;
      case "bool":
      case "byte":
        return BALOAD;
      case "char":
        return CALOAD;
      case "short":
        return SALOAD;
      default:
        return AALOAD;
    }

  }
  //---------
  public int icvOp(int value){
    if(value < 6){
      switch (value) {
        case 0:
          return  ICONST_0;
        case 1:
          return ICONST_1;
        case 2:
          return  ICONST_2;
        case 3:
          return ICONST_3;
        case 4:
          return ICONST_4;
        default:
          return ICONST_5;
      }
    }

    else if(value < 128)
      return BIPUSH;

    else return SIPUSH;
  }


  public String compareSymbol(String name){

    switch (name) {
      case "COND_EQUAL":
        return  "==";
      case "COND_UNEQUAL":
        return "!=";
      case "GREATER_THAN_EQUAL":
        return ">=";
      case "SMALLER_THAN_EQUAL":
        return "<=";
      case "SMALLER_THAN":
        return "<";
      default:
        return ">";
    }
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

  public int jnzOp(String comp) {
    switch (comp) {
      case "==":
        return IF_ICMPEQ;
      case "!=":
        return IF_ICMPNE;
      case "<=":
        return IF_ICMPLE;
      case ">=":
        return IF_ICMPGE;
      case "<":
        return IF_ICMPLT;
      case ">":
        return IF_ICMPGT;
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
    //System.out.println("sem: "+ semantic);


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
        Descriptor argDscp = new VariableDescriptor((String) semanticStack.peek());
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
          System.out.println("function signature: "+signature);
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
      ////--------|A R R A Y|-------------------------------
      case "madscp":{
        var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);

        var name = semanticStack.pop();
        var leftType = semanticStack.pop();
        var rightType = lastToken.toString().split("_")[1].toLowerCase();
        //----error handler-----------------------------------------------
        if(!leftType.equals(rightType)){
          System.err.println("can not assign "+rightType+" to "+leftType);
          System.exit(505);
        }
        //---------------------------------------------------------------
        ArrayDescriptor arrayDSCP = new ArrayDescriptor(leftType);
        arrayDSCP.setAddress( dscp.innerTable.getSize() );
        dscp.innerTable.add(name, arrayDSCP);
        //----------PUT TYPE BACK IN THE STACK FOR CADSCP------------
        semanticStack.push(leftType);
        ///-------NAME BACK------
        semanticStack.push(name);


        break;
      }
      ////-------------------------------------------------
      case "cadscp":{
        var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
        var arrayName = semanticStack.pop();
        var arraySize = Integer.parseInt(lastValue);
        var type = semanticStack.pop();
        int opCode = icvOp(arraySize);

        if(arraySize < 6){
          dscp.mv.visitInsn(opCode);
        }
        else{
          dscp.mv.visitIntInsn(opCode, arraySize);
        }
        dscp.mv.visitIntInsn(NEWARRAY, makeArrayOp(type));

        //------store the array------------------------
        var arrDSCP = ((ArrayDescriptor)dscp.innerTable.getDSCP(arrayName));
        arrDSCP.upperBound = arraySize - 1;
        var adr = arrDSCP.getAddress();
        dscp.mv.visitVarInsn(ASTORE, adr);




        break;
      }

      ///----------------|A R R A Y - A C C E S S|--------------------------------------------
      case "push_element":{
        semanticStack.push(lastValue);
        helpStack.push(lastToken.toString());
        break;
      }
      //-----------------------------------------------------------------------
      case "array_assign": {
        var dscp = (FunctionDescriptor) st.getDSCP(currentFunc);
        var literal = semanticStack.pop();
        var elem = semanticStack.pop();
        var name = semanticStack.pop();
        System.out.println(name + "[" + elem + "]" + "=" + literal);
        var literalType = helpStack.pop();

        //-----------------------------------------
        ArrayDescriptor array = (ArrayDescriptor) dscp.innerTable.getDSCP(name);

        assert helpStack.peek() != null;

        if(!helpStack.peek().equals("IDENTIFIER")){
           var index = Integer.parseInt(elem);
          //--error index handler-----------
           if (array.upperBound < index || index < 0) {
            System.err.println("index " + index + " is out of bounds for length " + (array.upperBound + 1));
            System.exit(202);
           }

          ///----------------------------
          if (!literalType.equals("IDENTIFIER")) {

              //---error type handler-----
              if (!array.type.equals(literalType)) {
                System.err.println("type " + literalType + " is not assignable to " + array.type + " array");
                System.exit(505);
              }
              //-------------------------
              dscp.mv.visitVarInsn(ALOAD, array.getAddress());
              if (index < 6) {
                dscp.mv.visitInsn(icvOp(index));
              } else {
                dscp.mv.visitIntInsn(icvOp(index), index);
              }
              typeLdcInsn(dscp.mv, literalType, literal);
              dscp.mv.visitInsn(setElementOp(literalType));
            } else {
              var id = literal;
              var varDSCP = findDSCP(dscp.innerTable, id);

              dscp.mv.visitVarInsn(ALOAD, array.getAddress());

              if (index < 6)
                dscp.mv.visitInsn(icvOp(index));
              else dscp.mv.visitIntInsn(icvOp(index), index);

              dscp.mv.visitVarInsn(loadOp(varDSCP.type), varDSCP.getAddress());
              dscp.mv.visitInsn(setElementOp(varDSCP.type));

            }
       }
        else{
          var varDSCP = findDSCP(dscp.innerTable, elem);
          var adr = varDSCP.getAddress();
          if(!varDSCP.type.equals("int")){
            System.err.println(elem+" must be integer");
            System.exit(101);
          }
          dscp.mv.visitVarInsn(ALOAD, array.getAddress());
          dscp.mv.visitVarInsn(ILOAD, adr);

          if (!literalType.equals("IDENTIFIER")){
            typeLdcInsn(dscp.mv, literalType, literal);
            dscp.mv.visitInsn(setElementOp(literalType));
          }else{

            var id = literal;
            var idDSCP = findDSCP(dscp.innerTable, id);
            System.out.println(id+"<<");
            dscp.mv.visitVarInsn(loadOp(idDSCP.type), idDSCP.getAddress());
            dscp.mv.visitInsn(setElementOp(idDSCP.type));
          }


        }



        break;
      }

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
        VariableDescriptor varDscp = new VariableDescriptor((String) type);
        int address = functionDscp.innerTable.getSize();
        varDscp.setAddress(address);

        functionDscp.innerTable.add((String) name, varDscp);

        // 1 extra slots for double
        if(type.equals("double")||type.equals("long")){
          functionDscp.innerTable.add(null,null);
        }

        if(helpStack.peek().equals("IDENTIFIER")){ // if the right side is id , load it's value
        helpStack.pop();
        var adr = ((VariableDescriptor)functionDscp.innerTable.getDSCP((String) value)).getAddress();
        functionDscp.mv.visitVarInsn(loadOp((String) type), adr);
      }
      else typeLdcInsn(functionDscp.mv, type, value);//-push for every type

      functionDscp.mv.visitVarInsn(getOp(type), address);


      break;
    }

    //------------------------------------------------------------------------
    case "push_dcl_name":{
      String name = lastValue;
      var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
      var varDscp = ((VariableDescriptor)dscp.innerTable.getDSCP(name));
      Logger.log(" dcl name is: "+name+ " and dscp :"+varDscp);
      if(varDscp != null) Logger.error("variable "+name+" is already defined in this function");
      semanticStack.push(lastValue);

    }
    break;
    //-----------------------------------------------------------------------
    case "simple_assign":{
      var functionDscp = (FunctionDescriptor)st.getDSCP(currentFunc);
      var literal = semanticStack.pop();
      var name    = semanticStack.pop();

      if(!semanticStack.isEmpty()){
        var leftSide = semanticStack.pop();
        var index = Integer.parseInt(literal);
        var arrayName = name;
        ArrayDescriptor array = (ArrayDescriptor)functionDscp.innerTable.getDSCP(arrayName);
        functionDscp.mv.visitVarInsn(ALOAD, array.getAddress());

        if (index < 6)
          functionDscp.mv.visitInsn(icvOp(index));
        else functionDscp.mv.visitIntInsn(icvOp(index), index);

        functionDscp.mv.visitInsn( getElementOp(array.type) );
        var leftDSCP = findDSCP(functionDscp.innerTable, leftSide);
        //-----error handling for type mismatch-----
        if(!leftDSCP.type.equals(array.type)){
          System.err.println("type mismatch");
          System.exit(505);
        }
        ///-----------------------------------------
        functionDscp.mv.visitVarInsn(getOp(leftDSCP.type), leftDSCP.getAddress());




      }
      else{



      if(!functionDscp.innerTable.hasDefined(name)){
            System.err.println("compile error: variable "+name+" is not defined at function "+currentFunc+"()");
            System.exit(404);//terminate compilation
      }
      else{

            var varDscp = (VariableDescriptor)functionDscp.innerTable.getDSCP(name);
            if(!helpStack.peek().equals("IDENTIFIER")){
                helpStack.pop();
                var type = varDscp.type;

                //---loads for every type
                typeLdcInsn(functionDscp.mv,type,literal);
                functionDscp.mv.visitVarInsn(getOp(type), varDscp.getAddress());

            } else{
                var id = literal;
               // System.out.println(id);
                var varDSCP = findDSCP(functionDscp.innerTable, id);
                var adr = varDSCP.getAddress();
                var type = varDSCP.getType();
                functionDscp.mv.visitVarInsn(loadOp(type), adr);
                functionDscp.mv.visitVarInsn(getOp(type), varDscp.getAddress());
            }
          }
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
    //-------------R E C O R D -----------------------------------------------

    case "write_class":{
      currentRecord = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
      currentRecord.visit(V1_8, ACC_FINAL, OUTCLASS_NAME, null, SUPER_CLASS, null);

      break;
    }
      case "complete_record":{
        currentRecord.visitEnd();


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
      Stack<String> temp = new Stack<>();
      Stack<String> kind = new Stack<>();
      temp.push(semanticStack.pop());
      temp.push(semanticStack.pop());
      kind.push(helpStack.pop());
      kind.push(helpStack.pop());

      for (int i = 0; i < 2; i++) {

        if (kind.peek().equalsIgnoreCase("IDENTIFIER")) {
          kind.pop();
          var name = temp.pop();

          var varDscp = ((VariableDescriptor) dscp.innerTable.getDSCP(name));

          //search type and address in innerTable
          var address = varDscp.getAddress();
          var type = varDscp.getType();
          dscp.mv.visitVarInsn(loadOp(type), address);
          addType = type;


        } else {
          var literalType = kind.pop();
          var val = temp.pop();
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
      Stack<String> temp = new Stack<>();
      Stack<String> kind = new Stack<>();
      temp.push(semanticStack.pop());
      temp.push(semanticStack.pop());
      kind.push(helpStack.pop());
      kind.push(helpStack.pop());

      for (int i = 0; i < 2; i++) {

        if (kind.peek().equalsIgnoreCase("IDENTIFIER")) {
          kind.pop();
          var name = temp.pop();

          var varDscp = ((VariableDescriptor) dscp.innerTable.getDSCP(name));

          //search type and address in innerTable
          var address = varDscp.getAddress();
          var type = varDscp.getType();
          dscp.mv.visitVarInsn(loadOp(type), address);
          addType = type;


        } else {
          var literalType = kind.pop();
          var val = temp.pop();
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
    //--------------------------------------------------------------
      case "bitwise_and":{
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


        dscp.mv.visitInsn(IAND);
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
      case "bitwise_or":{
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


        dscp.mv.visitInsn(IOR);
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
      case "bitwise_xor":{
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


        dscp.mv.visitInsn(IXOR);
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
    case "call": {
      var dscp = (FunctionDescriptor) st.getDSCP(currentFunc);

      Stack<String> args = new Stack<>();
      Stack<String> argsType = new Stack<>();

      while (!helpStack.peek().equals("IDENTIFIER")) {
        argsType.push(helpStack.pop());
        args.push(semanticStack.pop());
      }
      var funcName = semanticStack.pop();

      if (funcName.equals("len")) {
         helpStack.pop();
         var str = args.pop();
         dscp.mv.visitLdcInsn(str);
         var toAddrress = dscp.innerTable.getSize();
         dscp.mv.visitVarInsn(ASTORE, toAddrress);
         dscp.mv.visitVarInsn(ALOAD, toAddrress);
         VariableDescriptor systemVar = new VariableDescriptor("string");
         systemVar.setAddress(toAddrress);
         dscp.innerTable.add("system_var", systemVar);
         dscp.mv.visitMethodInsn(INVOKEVIRTUAL,"java/lang/String","length", "()I", false );
         var adr =  dscp.innerTable.getSize();
         dscp.mv.visitVarInsn(ISTORE,adr);
         VariableDescriptor varDSCP = new VariableDescriptor("int");
         varDSCP.setAddress(adr);
         dscp.innerTable.add("system_function_temp", varDSCP);
         semanticStack.push("system_function_temp");
         helpStack.push("IDENTIFIER");



      }
      else{
        helpStack.pop();
        var callee = ((FunctionDescriptor) st.getDSCP(funcName));
        Stack<String> temp = new Stack<>();

        for (var x : callee.argumentTypes) {
          temp.push(x);
        }
        String argumentBuilder = "";
        while (!temp.isEmpty()) {
          argumentBuilder += mapper.map.get(temp.pop());

        }
        argumentBuilder = "(" + argumentBuilder + ")" + mapper.map.get(callee.type);
        //------------------------------
        while (!args.isEmpty()) {
          var type = argsType.pop();
          var literal = args.pop();
          typeLdcInsn(dscp.mv, type, literal);

        }
        dscp.mv.visitMethodInsn(INVOKESTATIC, OUTCLASS_NAME, funcName, argumentBuilder, false);

        if (!callee.type.equals("void")) {
          var toAddress = dscp.innerTable.getSize();
          dscp.mv.visitVarInsn(getOp(callee.type), toAddress);
          VariableDescriptor varDSCP = new VariableDescriptor(callee.type);
          varDSCP.setAddress(toAddress);
          dscp.innerTable.add("system_function_temp", varDSCP);
          semanticStack.push("system_function_temp");
          helpStack.push("IDENTIFIER");

        }
    }






      break;
    }
    //-----------------------------------------------------------------------
    case "compare_push":{
      semanticStack.push(compareSymbol(lastToken.toString()));
      break;


    }
    //------------------------------------------------------------------------
      case "jump_zero":{
        var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);

        var secondOp = semanticStack.pop();
        var comp = semanticStack.pop();
        var firstOp = semanticStack.pop();
        //-----------------------
        var type2 = helpStack.pop();var type1 = helpStack.pop();
        //--first operand load

        if(type1.equalsIgnoreCase("IDENTIFIER")){
         var varDSCP = findDSCP(dscp.innerTable, firstOp);
         var adr = varDSCP.getAddress();
         var type = varDSCP.getType();
         dscp.mv.visitVarInsn(loadOp(type), adr);// load the variable



       }else { typeLdcInsn(dscp.mv, type1, firstOp); }

       //----second operand load
        if(type2.equalsIgnoreCase("IDENTIFIER")){
          var varDSCP = findDSCP(dscp.innerTable, secondOp);
          var adr = varDSCP.getAddress();
          var type = varDSCP.getType();
          dscp.mv.visitVarInsn(loadOp(type), adr);// load the variable

        }else { typeLdcInsn(dscp.mv, type2, secondOp); }

        Label ifJump = new Label();
        dscp.mv.visitJumpInsn(compareOp(comp), ifJump);
        labelStack.push(ifJump);



        break;

      }
      //-----------------------------------------------------------------------
      case "visit_block_body":{
        var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
        System.out.println("body");
        //dscp.mv.visitCode();


        break;
      }
      //----------------------------------------------------------------
      case "visit_loop_body":{
        var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
        Label loopStart = new Label();
        labelStack.push(loopStart);
        dscp.mv.visitLabel(loopStart);

        System.out.println("body");



        break;
      }
      //------------------------------------------------------------
      case "jnz":{
        var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);

        var secondOp = semanticStack.pop();
        var comp = semanticStack.pop();
        var firstOp = semanticStack.pop();
        //-----------------------
        var type2 = helpStack.pop();var type1 = helpStack.pop();

        //--first operand load
        if(type1.equalsIgnoreCase("IDENTIFIER")){
          var varDSCP = findDSCP(dscp.innerTable, firstOp);
          var adr = varDSCP.getAddress();
          var type = varDSCP.getType();
          dscp.mv.visitVarInsn(loadOp(type), adr);// load the variable



        }else { typeLdcInsn(dscp.mv, type1, firstOp); }

        //----second operand load
        if(type2.equalsIgnoreCase("IDENTIFIER")){
          var varDSCP = findDSCP(dscp.innerTable, secondOp);
          var adr = varDSCP.getAddress();
          var type = varDSCP.getType();
          dscp.mv.visitVarInsn(loadOp(type), adr);// load the variable

        }else { typeLdcInsn(dscp.mv, type2, secondOp); }


         dscp.mv.visitJumpInsn(jnzOp(comp), labelStack.pop());






        break;
      }

      ///------------------------------------------------------------------------
      case "cjz":{
        var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
        Label  end = new Label();

       if(lastToken.toString().equals("ELSE"))
        {
          dscp.mv.visitJumpInsn(GOTO, end);
        }

        var label = labelStack.pop();
        dscp.mv.visitLabel(label);

        if(lastToken.toString().equals("ELSE"))
        {
          labelStack.push(end);
        }





        break;}

        ////--------------------------------------------------------------------
      case "cjp":{
        var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
        if(!labelStack.isEmpty())
          dscp.mv.visitLabel(labelStack.pop());

        System.out.println("end if");

        break;

      }

      ///-----------------------------------------------------------------
      case "exit":{
        var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
        var label = labelStack.pop();
        dscp.mv.visitLabel(label);
        break;
      }
      //----------------------------------------------------------
      case "jump_back":{
        var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
        dscp.mv.visitJumpInsn(GOTO, currentLabel);


        break;
      }
      //------------------------------------------------------
      case "complete_jump_back":{
        var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
        currentLabel = new Label();
        //labelStack.push(dest);
        dscp.mv.visitLabel(currentLabel);
        break;
      }
      //-------------------------------------------------------------------------
      case "plus_plus":{
        var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
        var varDSCP = findDSCP(dscp.innerTable, semanticStack.pop());
        dscp.mv.visitIincInsn(varDSCP.getAddress(), +1);

        break;
      }
      case "minus_minus":{
        var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
        var varDSCP = findDSCP(dscp.innerTable, semanticStack.pop());
        dscp.mv.visitIincInsn(varDSCP.getAddress(), -1);

        break;
      }

      case "plus_assign":{
        var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);

        break;
      }

      //--------------------------------------------------------------------
      case "load_return":{
        var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
        String value = null;
        if(!semanticStack.isEmpty()){
          value = semanticStack.pop();
        }
       if(!helpStack.peek().equals("IDENTIFIER")){
         typeLdcInsn(dscp.mv, helpStack.pop(), value);
       }
       else{
         var id = value;
         var varDSCP = findDSCP(dscp.innerTable, id);
         dscp.mv.visitVarInsn(loadOp(varDSCP.getType()), varDSCP.getAddress());

       }

        break;
      }


    ///-------------------------------------------------------------------------
      case "end_function":{
         var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
         while (!semanticStack.isEmpty()) semanticStack.pop();
         dscp.mv.visitInsn(returnOp(dscp.type));
         dscp.mv.visitMaxs(dscp.innerTable.getSize() + 2, dscp.innerTable.getSize() + 1);
         dscp.mv.visitEnd();


         break;
    }




    case "done":
    System.out.println("ok done");
    break;

  }

}



  private VariableDescriptor findDSCP(SymbolTable table, String key) {
    return ((VariableDescriptor)table.getDSCP(key));
  }


}
