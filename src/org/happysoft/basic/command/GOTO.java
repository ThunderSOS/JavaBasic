
package org.happysoft.basic.command;

import org.happysoft.basic.Program;
import org.happysoft.basic.Statement;
import org.happysoft.basic.SyntaxError;


/**
 * @author Chris
 * GOTO line
 * Continue execution from the stated line number. 
 */
public class GOTO extends AbstractCommand {

  @Override
  public StructureElement[] getCommandStructure() {
    return new StructureElement[] { StructureElement.EXPRESSION };
  }  
  
  @Override
  public void execute(Program context, Statement statement) throws SyntaxError {
    context.setLineAndStatement((int)statement.getExpressions()[0].eval().getArgument().getIntValue(), 0);
  }

}

