
package org.happysoft.basic.command;

import org.happysoft.basic.Program;
import org.happysoft.basic.Statement;
import org.happysoft.basic.SyntaxError;


/**
 * @author Chris
 */
public class NEXT extends AbstractCommand {

  @Override
  public StructureElement[] getCommandStructure() {
    return new StructureElement[] { StructureElement.IDENTIFIER };
  }  
  
  @Override
  public void execute(Program context, Statement statement) throws SyntaxError {
    ForVariableCache fors = ForVariableCache.getInstance();
    String var = statement.getIdentifiers()[0];
    boolean jump = fors.next(var);
    if(jump) {
      int[] jumpTo = fors.getGotoJump(var);
      context.setLineAndStatement(jumpTo[0], jumpTo[1]+1);      
    } else {
      System.out.println("Continue to next statement");
    }
  }
}

