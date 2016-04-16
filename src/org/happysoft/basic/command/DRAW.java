
package org.happysoft.basic.command;

import org.happysoft.basic.Program;
import org.happysoft.basic.Statement;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Expression;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class DRAW extends AbstractCommand {
  
  @Override
  public StructureElement[] getCommandStructure() {
    return new StructureElement[] { StructureElement.EXPRESSION_LIST };
  }
    
  @Override
  public ExtendedMode[] getExtendedModes() {
    return new ExtendedMode[] {ExtendedMode.OVER};
  }
  
  @Override
  public void execute(Program context, Statement statement) throws SyntaxError {
    Expression[] expressions = statement.getExpressions();
    int x1 = (int)expressions[0].eval().getArgument().getIntValue();
    int y1 = (int)expressions[1].eval().getArgument().getIntValue(); 
    context.getDisplayWindow().drawLine(x1, y1);
  }
}
