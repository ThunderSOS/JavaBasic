
package org.happysoft.basic.command;

import org.happysoft.basic.Program;
import org.happysoft.basic.Statement;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Expression;

/**
 * @author Chris
 */
public class PRINT extends AbstractCommand {
  
  @Override
  public StructureElement[] getCommandStructure() {
    return new StructureElement[] { StructureElement.EXPRESSION_LIST };
  }
  
  @Override
  public ExtendedMode[] getExtendedModes() {
    return new ExtendedMode[] { ExtendedMode.AT, ExtendedMode.TAB };
  }
  
  @Override
  public void execute(Program context, Statement statement) throws SyntaxError {
    if(ExtendedMode.AT == extendedMode) {
      Expression[] expressions = statement.getExtendedModeExpressions();
      if(expressions == null || expressions.length != 2) {
        throw new SyntaxError("Exactly two extended mode arguments expected with PRINT AT");
      }
      int x1 = (int)expressions[0].eval().getArgument().getIntValue();
      int y1 = (int)expressions[1].eval().getArgument().getIntValue(); 
      context.getDisplayWindow().at(x1, y1);
    } 
    
    for(Expression e : statement.getExpressions()) {
      String str = e.eval().getArgument().getStringValue();
      System.out.println(str);
      context.getDisplayWindow().drawString(str);
    }
    
  }
}
