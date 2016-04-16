
package org.happysoft.basic.command;

import org.happysoft.basic.Program;
import org.happysoft.basic.Statement;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Expression;
import org.happysoft.basic.expression.ExpressionResult;
import org.happysoft.basic.var.NumericArrayTable;
import org.happysoft.basic.var.StringArrayTable;
import org.happysoft.basic.var.StringTable;
import org.happysoft.basic.var.VariableTable;


/**
 * @author Chris
 */
public class LET extends AbstractCommand {

  @Override
  public StructureElement[] getCommandStructure() {
    return new StructureElement[] { 
      StructureElement.ASSIGNMENT 
    };
  }

  @Override
  public void execute(Program context, Statement statement) throws SyntaxError {
    
    String assignmentVariable = statement.getAssignmentVariables()[0];
    Expression assignmentExpression = statement.getAssignmentExpression();
    ExpressionResult assignmentValue = assignmentExpression.eval();    
    Expression[] indexExpressions = statement.getExpressions();
    
    int[] dimensions = null;
    boolean isArrayAssignment = false;
    
    if(indexExpressions.length > 0) {
      dimensions = new int[indexExpressions.length];
      isArrayAssignment = true;
      for(int i = 0; i < indexExpressions.length; i++) {
        ExpressionResult r = indexExpressions[i].eval();
        dimensions[i] = (int)r.getArgument().getIntValue();
      }
    }
     
    if(assignmentValue.getResultType() == ExpressionResult.ResultType.NUMERIC) {
      if (assignmentVariable.endsWith("$")) {
        throw new SyntaxError("Numeric assignment to string variable");
      }
      double value = assignmentValue.getArgument().getDoubleValue();
      if (isArrayAssignment) {
        NumericArrayTable.getInstance().setValue(assignmentVariable, value, dimensions);
      } else {
        VariableTable.getInstance().setVariable(assignmentVariable, value);
      }
    }  
    
    if(assignmentValue.getResultType() == ExpressionResult.ResultType.STRING) {
      if (!assignmentVariable.endsWith("$")) {
        throw new SyntaxError("String assignment to non string variable");
      }
      String string = assignmentValue.getArgument().getStringValue();
      if (isArrayAssignment) {
        StringArrayTable.getInstance().setValue(assignmentVariable, string, dimensions);
      } else {
        StringTable.getInstance().setStringVariable(assignmentVariable, string);
      }
    }
  }
  
}
