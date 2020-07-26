package compiler.codegen;

import compiler.scanner.symboltable.Descriptor;
import compiler.scanner.symboltable.FunctionDescriptor;
import compiler.scanner.symboltable.SymbolTable;
import compiler.scanner.symboltable.VariableDescriptor;
import jdk.swing.interop.SwingInterOpUtils;
import org.objectweb.asm.*;
import java.io.*;
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
    public  Stack<String> semanticStack;
    public String currentFunc;

    private final Lexical lexical;


    public CodeGen(File output, Lexical lexical) {
        OUTPUT_FILE = output;
        OUTCLASS_NAME = Utils.getClassNameFromFile(output);
        this.lexical = lexical;
        this.semanticStack = new Stack<>();
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

        try(OutputStream out = new FileOutputStream(OUTPUT_FILE)){
            out.write(mainCLW.toByteArray());
            Logger.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void writeRecordClass(){
        Logger.log("writing record to file");
        try(OutputStream out = new FileOutputStream(OUTPUT_FILE)){
            out.write(structCLW.toByteArray());
        } catch(IOException e){
            e.printStackTrace();
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


    public void doSemantic(String semantic){

        TypeDescMap mapper = new TypeDescMap();

        semantic = semantic.replace("@","");
        var st = lexical.symTab;
        System.out.println("semantic:"+semantic);
        String lastValue = lexical.getLastSym().content;
        var lastToken = lexical.getLastSym().token;

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

                break;
            }

            //-----------------------------------------
            case "complete_assign":{
                var value = semanticStack.pop();
                var type = semanticStack.pop();

                var functionDscp = (FunctionDescriptor)st.getDSCP(currentFunc);

                switch (type) {
                    case "double":
                        functionDscp.mv.visitLdcInsn(Double.parseDouble(value));
                        break;
                    case "float":
                        functionDscp.mv.visitLdcInsn(Float.parseFloat(value));
                        break;
                    case "long":
                        functionDscp.mv.visitLdcInsn(Long.parseLong(value));
                        break;
                    case "int":
                        functionDscp.mv.visitLdcInsn(Integer.parseInt(value));
                        break;
                    case "byte":
                        functionDscp.mv.visitLdcInsn(Byte.parseByte(value));
                        break;
                    default:
                        functionDscp.mv.visitLdcInsn(value);
                        break;
                }

                int storage = functionDscp.innerTable.getSize()+1;
                functionDscp.mv.visitVarInsn(getOp(type), storage);

                break;
            }

            //----------------------
            case "make_dscp":{
                var functionDscp = (FunctionDescriptor)st.getDSCP(currentFunc);
                VariableDescriptor varDscp = new VariableDescriptor(semanticStack.peek());
                functionDscp.innerTable.add(lastValue, varDscp);

                break;

            }


            //---------------------
            case "push_id":{

               var symtab = ((FunctionDescriptor)st.getDSCP(currentFunc)).innerTable;
               if(symtab.hasDefined(lastValue)){
                   semanticStack.push(lastValue);

               }
               else System.err.println("compile error: variable "+lastValue+" not defined!");

            }
            break;


            //----------------------------------------------
            case "print":{

                var literal = lastToken.toString().toLowerCase();
                String param = null;

                if(literal.equals("icv"))
                    param = "I";

                else{
                    String key = literal.trim().split("_")[0];
                    param = mapper.map.get(key); }

                String argType = "("+param+")";
                var dscp = (FunctionDescriptor)st.getDSCP(currentFunc);
                dscp.mv.visitCode();
                dscp.mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                //pushes the data on the stack
                dscp.mv.visitLdcInsn(lastValue);
                //print call
                dscp.mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", argType+"V", false);


                break;
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
