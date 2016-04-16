
package org.happysoft.basic.command;

import org.happysoft.basic.Program;
import org.happysoft.basic.Statement;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Expression;

/**
 *
 * @author Chris
 */
public class WINDOW extends AbstractCommand {

  @Override
  public StructureElement[] getCommandStructure() {
    return new StructureElement[] { StructureElement.EXPRESSION_LIST };
  }  
  
  @Override
  public void execute(Program context, Statement statement) throws SyntaxError {
    Expression[] expressions = statement.getExpressions();
    String name = expressions[0].eval().getArgument().getStringValue();
    int width = (int)expressions[1].eval().getArgument().getIntValue();
    int height = (int)expressions[2].eval().getArgument().getIntValue(); 
    context.getDisplayWindow().openWindow(name, width, height);
  }
}