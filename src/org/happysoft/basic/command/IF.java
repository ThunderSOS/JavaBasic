
package org.happysoft.basic.command;

import org.happysoft.basic.Program;
import org.happysoft.basic.Statement;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.ExpressionResult;

/**
 * @author Chris
 */
public class IF extends AbstractCommand {

  @Override
  public StructureElement[] getCommandStructure() {
    return new StructureElement[] { 
      StructureElement.EXPRESSION, 
      StructureElement.COMMAND 
    };
  }
  
  @Override
  public ConnectorElement[] getExpectedConnectives() {
    return new ConnectorElement[] { ConnectorElement.THEN };
  }

  @Override
  public void execute(Program context, Statement statement) throws SyntaxError {
    ExpressionResult result = statement.getExpressions()[0].eval();
    if (!(result.getResultType() == ExpressionResult.ResultType.BOOLEAN)) {
      throw new SyntaxError("Boolean expression required");
    }
    if (result.getArgument().getIntValue() == 1) {
      Statement s = statement.getNextStatement();
      if (s == null) {
        throw new SyntaxError("Statement parse error");
      }
      s.getCommand().execute(context, s);
    }
  }

}