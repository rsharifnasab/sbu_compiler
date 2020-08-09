package compiler.codegen;

//import org.objectweb.asm.*;
import java.io.*;
import java.util.*;

import compiler.util.*;
import compiler.scanner.*;

import compiler.ast.*;

import compiler.ast.block.*;
import compiler.ast.access.*;

import compiler.ast.block.stmt.*;
import compiler.ast.block.stmt.loop.*;
import compiler.ast.block.stmt.function.*;
import compiler.ast.block.stmt.assignment.*;
import compiler.ast.block.stmt.conditional.ifstmt.*;
import compiler.ast.block.stmt.conditional.casestmt.*;

import compiler.ast.dcl.*;
import compiler.ast.dcl.array.*;
import compiler.ast.dcl.variable.*;

import compiler.ast.expr.*;
import compiler.ast.expr.other.*;
import compiler.ast.expr.constant.*;

import compiler.ast.expr.binary.logical.*;
import compiler.ast.expr.binary.arithmatic.*;

import compiler.ast.expr.unary.logical.*;
import compiler.ast.expr.unary.arithmatic.*;
import compiler.ast.expr.unary.arithmatic.dual.*;

import compiler.ast.program.*;
import compiler.ast.program.global.*;
import compiler.ast.program.function.*;
import compiler.ast.program.structure.*;

import compiler.ast.type.*;

import static org.objectweb.asm.Opcodes.*;

public class CodeGen {

    private final Lexical lexical;
    private final Deque<Object> semanticStack;

    public CodeGen(Lexical lexical) {
        this.lexical = lexical;
        this.semanticStack = new LinkedList<>();
    }

    public void doSemantic(String semantic){
        semantic = semantic.replace("@","");
        String lastValue = lexical.getLastSym().content;

        Logger.print("cg("+semantic+") \tscanner last:"+lexical.getLastSym(),"GREEN");

        switch(semantic){
            case "TYPE_FLOAT":
                semanticStack.push(Type.FLOAT);
            break;
            case "TYPE_DOUBLE":
                semanticStack.push(Type.DOUBLE);
            break;
            case "TYPE_LONG":
                semanticStack.push(Type.LONG);
            break;
            case "TYPE_CHAR":
            case "TYPE_BOOL":
            case "TYPE_INT":
                semanticStack.push(Type.INT);
            break;
            case "TYPE_STRING":
                semanticStack.push(Type.STRING);
            break;
            case "TYPE_VOID":
                semanticStack.push(Type.VOID);
            break;
            case "TYPE_AUTO":
                semanticStack.push(Type.AUTO);
            break;
            case "TYPE_ID": // user defined type
                semanticStack.push(new Type(lastValue));
            break;

            case "PUSH_ID":
                semanticStack.push(lastValue);
            break;

            case "CHAR_LIT":
                semanticStack.push(
                  new CharConstant(lastValue)
                );
            break;
            case "INT_LIT":
            case "LONG_LIT":
                semanticStack.push(
                  new IntegerConstant(lastValue)
                );
            break;
            case "FLOAT_LIT":
            case "DOUBLE_LIT":
                semanticStack.push(
                  new DoubleConstant(lastValue)
                );
            break;
            case "STR_LIT":
                semanticStack.push(
                  new StringConstant(lastValue)
                  );
            break;



            case "global_var_dcl":
            {
                String id = (String) semanticStack.pop();
                Type type = (Type) semanticStack.pop();
                ProgramContent cont = new GlobalVarDCL(type, id);
                Program.getInstance().addContent(cont);
            }break;

            case "init_function":
              FunctionArguments.getInstance().init();
            break;

            case "func_dcl":
            {
              Block b = (Block) semanticStack.pop();
              String name = (String) semanticStack.pop();
              Type retType = (Type) semanticStack.pop();

              ProgramContent cont = new FunctionDCL(retType, name, b);
              Program.getInstance().addContent(cont);
            } break;

            case "EMPTY_FUNC_BLOCK":
              semanticStack.push(null); // block e dasturat nadare
            break;

            case "BLOCK":
              semanticStack.push(new Block());
            break;

            case "END_BLOCK":
              Blocks.getInstance().remove();
            break;

            case "add_arg":
            {
              String name = (String) semanticStack.pop();
              Type t = (Type) semanticStack.pop();
              FunctionArguments.getInstance().addArgument(name, t);
              /*
              TODO array
              function_array_dcl:b
               t = b ? Type.toArray(t):t;
               arguments ::=
                   variable_type:t id:id function_array_dcl:b {:t = b ? Type.toArray(t) : t;:} arguments_part {:FunctionArguments.getInstance().addArgument(id, t);:}
               function_array_dcl ::=
                   BRACKOP BRACKCL function_array_dcl {:RESULT = true;:}
                   |
               {:RESULT = false;:}
                   ;
              */
            }break;
            case "no_args":

            break;

            case "RETURN":
              Blocks.getInstance().getCurrent().addContent(
                        new Return(
                            (Expression) semanticStack.pop()
                        )
                    );
            break;
            case "CONTINUE":
              Blocks.getInstance().getCurrent().addContent(
                  new Continue()
                );
            break;
            case "BREAK":
              Blocks.getInstance().getCurrent().addContent(
                  new Break()
                );
            break;

            case "ADD_TO_BLOCK":
                Blocks.getInstance().getCurrent().addContent((BlockContent) semanticStack.pop());
            break;

            case "START_FUNC_CALL":
                FunctionAccessData.getInstance().init();
            break;

            case "DONE_FUNC_CALL":
            {
              String name = (String) semanticStack.pop();
              FunctionAccess fa = new FunctionAccess(
                  FunctionAccessData.getInstance().getParameters()
              );
              fa.setDescriptor(name);
              semanticStack.push(new FunctionCall(fa));
            }break;

            case "ADD_FUNC_PARAM":
            {
              Expression e = (Expression) semanticStack.pop();
              FunctionAccessData.getInstance().addParameter(e);
            }break;

            case "RECORD_INIT":
                Logger.print("record init","YELLOW");
                Structures.getInstance().init();

            break;

            case "RECORD_VAR_DCL":
            {
            /*    
              Structures.getInstance().addDCL(dcl);
              CONST struct_var_dcl_part:dcl SEMI {:dcl.getDescriptor().setConst(true); RESULT = dcl;:}
              struct_var_dcl_part:dcl SEMI {:dcl.getDescriptor().setConst(false); RESULT = dcl;:}

                struct_var_dcl_part ::=
              struct_type:t id:id struct_var_dcl_cnt:dcl {:dcl.getDescriptor().setType(t); dcl.getDescriptor().setName(id); RESULT = dcl;:}

                struct_var_dcl_cnt ::=
              ASSIGN const_val:c {:RESULT = new StructVarDCL(c);:}
               {:RESULT = new StructVarDCL(null);:}
               */
            } break;

            case "RECORD_DONE":
            {
                Logger.print("record done","YELLOW");
                String id = (String) semanticStack.pop();
                ProgramContent cont = Structures.getInstance().getDCL(id);
                Program.getInstance().addContent(cont);
            } break;




          /*

        TRUE {:RESULT = new BooleanConstant(true);:}

          FALSE {:RESULT = new BooleanConstant(false);:}

            cond_stmt:stmt {:Blocks.getInstance().getCurrent().addContent(stmt);:}
          loop_stmt:stmt {:Blocks.getInstance().getCurrent().addContent(stmt);:}
          assignment:stmt SEMI {:Blocks.getInstance().getCurrent().addContent(stmt);:}
          function_call:a SEMI {:Blocks.getInstance().getCurrent().addContent(new FunctionCall(a));:}

          //blocks
          block ::=
              BEGIN {:RESULT = new Block();:} block_content END {:Blocks.getInstance().remove();:}
              ;

          block_content ::=
              var_dcl block_content
              |
              statement block_content
              |

              ;


          //variables
          var_dcl ::=
              CONST {:Variables.getInstance().setConstant(true);:} var_dcl_part SEMI {:Variables.getInstance().setConstant(false);:}
              |
              var_dcl_part SEMI
              ;

          var_dcl_part ::=
              variable_type:t {:Variables.getInstance().setType(t);:} var_dcl_cnt:cont {:Blocks.getInstance().getCurrent().addContent(cont);:} var_dcl_cnt_extension
              ;

          var_dcl_cnt ::=
              single_var:dcl var_dcl_cnt_part:e {:RESULT = new CompleteDCL(dcl, e);:}
              ;

          var_dcl_cnt_part ::=
              ASSIGN expr:e {:RESULT = e;:}
              |
          {:RESULT = null;:}
              ;

          var_dcl_cnt_extension ::=
              COMA var_dcl_cnt:cont {:Blocks.getInstance().getCurrent().addContent(cont);:} var_dcl_cnt_extension
              |

              ;

          single_var ::=
              id:id array_part:dcl {:dcl.setId(id); RESULT = dcl;:}
              ;

          array_part ::=
              BRACKOP expr:e BRACKCL array_part {:RESULT = new ArrayDCL(e);:}
              |
         {:RESULT = new VariableDCL();:}
              ;




          //conditionals
          cond_stmt ::=
              IF PRANTOP expr:e PRANTCL block:ifb else_part:eb {:RESULT = new If(e, ifb, eb);:}
              |
              SWITCH PRANTOP variable:a {:Cases.getInstance().init();:} PRANTCL OF COL BEGIN case_part DEFAULT COL block:db END {:RESULT = new Switch(a, db);:}
              ;

          else_part ::=
              ELSE block:b {:RESULT = b;:}
              |
           {:RESULT = null;:}
              ;

          case_part ::=
              CASE int_lit:i COL block:b {:Cases.getInstance().addCase(i, b);:} case_part
              |
              ;


          //loops
          loop_stmt ::=
              FOR PRANTOP loop_init_part:in SEMI loop_cond_part:e SEMI loop_update_part:up PRANTCL block:b {:RESULT = new For(in, e, up, b);:}
              |
              REPEAT block:b UNTIL PRANTOP expr:e PRANTCL SEMI {:RESULT = new Repeat(b, e);:}
              |
              FOREACH PRANTOP id:i1 IN id:i2 PRANTCL block:b {:RESULT = new Foreach(i1, i2, b);:}
              ;

          loop_init_part ::=
              assignment:a {:RESULT = a;:}
              |
       {:RESULT = null;:}
              ;

          loop_cond_part ::=
              expr:e {:RESULT = e;:}
              |
           {:RESULT = null;:}
              ;

          loop_update_part ::=
              assignment:a {:RESULT = a;:}
              |
       {:RESULT = null;:}
              ;


          //assignments
          assignment ::=
              variable:a ASSIGN expr:e {:RESULT = new DirectAssign(a, e);:}
              |
              variable:a PLUSASSIGN expr:e {:RESULT = new PlusAssign(a, e);:}
              |
              variable:a MINUSASSIGN expr:e {:RESULT = new MinusAssign(a, e);:}
              |
              variable:a MULTASSIGN expr:e {:RESULT = new MultiplyAssign(a, e);:}
              |
              variable:a DIVASSIGN expr:e {:RESULT = new DivisionAssign(a, e);:}
              ;

          variable ::=
              var_access:a {:RESULT = a;:}
              |
              variable:a DOT id:id {:RESULT = new StructureAccess(a); RESULT.setDescriptor(id);:}
              ;

          var_access ::=
              id:id var_access_array_part:a {:a.setDescriptor(id); RESULT = a;:}
              ;

          var_access_array_part ::=
              BRACKOP expr:e BRACKCL array_part {:RESULT = new ArrayAccess(e);:}
              |
 {:RESULT = new VariableAccess();:}
              ;

          //expressions
          expr ::=
              const_val:e {:RESULT = e;:}
              |
              PRANTOP expr:e PRANTCL {:RESULT = e;:}
              |
              NOT expr:e {:RESULT = new Not(e);:}
              |
              BINOT expr:e {:RESULT = new BinaryNot(e);:}
              |
              MINUS expr:e {:RESULT = new Uminus(e);:} %prec UMINUS
              |
              PREFMINUS2 variable:a {:RESULT = new PrefixMinus2(a);:}
              |
              PREFPLUS2 variable:a {:RESULT = new PrefixPlus2(a);:}
              |
              variable:a PREFMINUS2 {:RESULT = new PostfixMinus2(a);:} %prec POSTMINUS2
              |
              variable:a PREFPLUS2 {:RESULT = new PostfixPlus2(a);:} %prec POSTPLUS2
              |
              PRANTOP casting_type:t PRANTCL expr:e {:RESULT = new Casting(t, e);:} %prec CASTPRANTOP
              |
              SIZEOF PRANTOP variable_type:t PRANTCL {:RESULT = new SizeOf(t);:}
              |
              arithmatic_expr:e {:RESULT = e;:}
              |
              conditional_expr:e {:RESULT = e;:}
              |
              variable:a {:RESULT = new Variable(a);:}
              |
              function_call:a {:RESULT = new ast.expr.function.FunctionCall(a);:}
              ;

          casting_type ::=
              CHAR {:RESULT = Type.INT;:}
              |
              INT {:RESULT = Type.INT;:}
              |
              FLOAT {:RESULT = Type.FLOAT;:}
              |
              DOUBLE {:RESULT = Type.DOUBLE;:}
              ;

          arithmatic_expr ::=
              expr:e1 MULT expr:e2 {:RESULT = new Mult(e1, e2);:}
              |
              expr:e1 DIVIDE expr:e2 {:RESULT = new Div(e1, e2);:}
              |
              expr:e1 MOD expr:e2 {:RESULT = new Mod(e1, e2);:}
              |
              expr:e1 PLUS expr:e2 {:RESULT = new Add(e1, e2);:}
              |
              expr:e1 MINUS expr:e2 {:RESULT = new Sub(e1, e2);:}
              |
              expr:e1 BIAND expr:e2 {:RESULT = new BinaryAnd(e1, e2);:}
              |
              expr:e1 BIOR expr:e2 {:RESULT = new BinaryOr(e1, e2);:}
              |
              expr:e1 BIEXOR expr:e2 {:RESULT = new BinaryXor(e1, e2);:}
              ;

          conditional_expr ::=
              expr:e1 LT expr:e2 {:RESULT = new LT(e1, e2);:}
              |
              expr:e1 LTEQ expr:e2 {:RESULT = new LE(e1, e2);:}
              |
              expr:e1 GT expr:e2 {:RESULT = new GT(e1, e2);:}
              |
              expr:e1 GTEQ expr:e2 {:RESULT = new GE(e1, e2);:}
              |
              expr:e1 EQEQ expr:e2 {:RESULT = new EQ(e1, e2);:}
              |
              expr:e1 NOTEQ expr:e2 {:RESULT = new NE(e1, e2);:}
              |
              expr:e1 AND expr:e2 {:RESULT = new BinaryAnd(e1, e2);:}
              |
              expr:e1 OR expr:e2 {:RESULT = new BinaryOr(e1, e2);:}
              ;




          */

            default:
                Logger.print("semantic is:"+semantic+", and i cant' run it","RED");
        }



    }

}
