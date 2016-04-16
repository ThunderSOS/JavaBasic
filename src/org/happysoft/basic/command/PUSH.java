
package org.happysoft.basic.command;

import org.happysoft.basic.Program;
import org.happysoft.basic.Statement;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.Expression;
import org.happysoft.basic.expression.ExpressionResult;

/**
 * @author Chris
 */
public class PUSH extends AbstractCommand {

  @Override
  public StructureElement[] getCommandStructure() {
    return new StructureElement[] { StructureElement.EXPRESSION_LIST };
  }

  @Override
  public void execute(Program context, Statement statement) throws SyntaxError {
    Expression[] expression = statement.getExpressions();
    PushStack stack = PushStack.getInstance();
    for(Expression e : expression) {
      ExpressionResult res = e.eval();
      Argument a = res.getArgument();
      stack.push(a);
    }
   
  }
  
}