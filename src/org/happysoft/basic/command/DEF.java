
package org.happysoft.basic.command;

import org.happysoft.basic.Program;
import org.happysoft.basic.Statement;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Expression;
import org.happysoft.basic.expression.function.FunctionFactory;
import org.happysoft.basic.expression.function.UserDefinedFunction;

/**
 * @author Chris
 * DEFine a user defined function (UDF)
 * e.g. DEF seconds()=INT(time()/1000) 
 * UDF's may make use of other already existing UDF's. 
 */
public class DEF extends AbstractCommand {

  @Override
  public StructureElement[] getCommandStructure() {
    return new StructureElement[] { StructureElement.IDENTIFIER, StructureElement.FUNCTION_ASSIGNMENT };
  }

  @Override
  public void execute(Program context, Statement statement) throws SyntaxError {
    String fnName = statement.getIdentifiers()[0];
    String[] localVariables = statement.getAssignmentVariables();
    Expression expression = statement.getAssignmentExpression();
    UserDefinedFunction udf = new UserDefinedFunction(expression.getExpression(), localVariables);   
    FunctionFactory.getInstance().addUDF(fnName, udf);
  }
  
}