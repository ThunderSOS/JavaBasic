
package org.happysoft.basic.command;

import org.happysoft.basic.Program;
import org.happysoft.basic.Statement;
import org.happysoft.basic.SyntaxError;


/**
 * @author Chris
 */
public class RETURN extends AbstractCommand {

  @Override
  public StructureElement[] getCommandStructure() {
    return new StructureElement[] { StructureElement.EXPRESSION };
  }  
  
  @Override
  public void execute(Program context, Statement statement) throws SyntaxError {
    GoSubStack stack = GoSubStack.getInstance();
    int[] jumpTo = stack.getReturnJump();
    System.out.println("return to " + jumpTo[0] + ":" + (jumpTo[1]+1));
    context.setLineAndStatement(jumpTo[0], jumpTo[1]+1);      
  }
}
