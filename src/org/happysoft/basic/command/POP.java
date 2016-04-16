
package org.happysoft.basic.command;

import java.util.EmptyStackException;
import org.happysoft.basic.Program;
import org.happysoft.basic.Statement;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.TokenType;
import org.happysoft.basic.var.StringTable;
import org.happysoft.basic.var.VariableTable;


/**
 * @author Chris
 */
public class POP extends AbstractCommand {

  @Override
  public StructureElement[] getCommandStructure() {
    return new StructureElement[] { StructureElement.IDENTIFIER_LIST };
  }  
  
  @Override
  public void execute(Program context, Statement statement) throws SyntaxError {
    PushStack stack = PushStack.getInstance();
    String[] vars = statement.getIdentifiers();
    try {
      for(String var : vars) {
        Argument arg = stack.pop();
        if(arg.getTokenType() == TokenType.NUMBER) {
          if (var.endsWith("$")) {
            throw new SyntaxError("Numeric assignment to string variable");
          }
          double value = arg.getDoubleValue();
          VariableTable.getInstance().setVariable(var, value);
        }    
        if(arg.getTokenType() == TokenType.STRING) {
          if (!var.endsWith("$")) {
            throw new SyntaxError("String assignment to non string variable");
          }
          String string = arg.getStringValue();
          StringTable.getInstance().setStringVariable(var, string);
        }
      }
    } catch (EmptyStackException ese) {
      throw new SyntaxError("More POPs than PUSHes");
    }
  }
}

