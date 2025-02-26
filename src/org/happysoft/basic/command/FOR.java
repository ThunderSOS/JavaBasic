package org.happysoft.basic.command;

import org.happysoft.basic.Program;
import org.happysoft.basic.Statement;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Expression;
import org.happysoft.basic.expression.ExpressionResult;

/**
 * @author Chris Implements the FOR control loop
 */
public class FOR extends AbstractCommand {

  @Override
  public StructureElement[] getCommandStructure() {
    return new StructureElement[]{
      StructureElement.ASSIGNMENT,
      StructureElement.EXPRESSION,
      StructureElement.EXPRESSION
    };
  }

  @Override
  public ConnectorElement[] getExpectedConnectives() {
    return new ConnectorElement[]{ConnectorElement.TO, ConnectorElement.STEP};
  }

  @Override
  public void execute(Program context, Statement statement) throws SyntaxError {
    ForVariableCache fors = ForVariableCache.getInstance();
    if (statement.getAssignmentVariables().length != 1) {
      throw new SyntaxError("Can only have 1 control variable in a FOR loop");
    }
    String varName = statement.getAssignmentVariables()[0];
    Expression assignmentExpression = statement.getAssignmentExpression();
    ExpressionResult assignmentValue = assignmentExpression.eval();
    Expression[] toAndStep = statement.getExpressions();
    long from = assignmentValue.getArgument().getIntValue();
    long to, step = 1;
    to = toAndStep[0].eval().getArgument().getIntValue();
    if (toAndStep.length == 2) {
      step = toAndStep[1].eval().getArgument().getIntValue();
    }
    if ((to < from && step > 0) || (from < to && step < 0)) {
      // then the loop contents should not be executed at all so we must scan forward for the next
      System.out.println("Scanning for next");
      context.scanFor(NEXT.class, varName, context.getCurrentLine(), context.getCurrentStatement(), false);
    } else {
      fors.initializeForLoop(
              statement.getLineNumber(), statement.getStatementInLine(), varName, from, to, step);
    }
  }
  
}
