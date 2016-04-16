
package org.happysoft.basic.expression.function;

import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.*;

/**
 * @author Chris
 */
public class UserDefinedFunction extends Function {
  
  private String expression = null;
  private String[] localVariables = null;
  
  public UserDefinedFunction(String expression, String[] args) throws SyntaxError {
    this.expression = expression;
    this.localVariables = args;
  }
  
  public Argument eval(Argument... args) throws SyntaxError {
    LocalArgumentMap localArgs = new LocalArgumentMap();
    for(int i = 0; i < args.length; i++) {
      localArgs.addKey(localVariables[i], args[i]);
    }
    ExpressionResult result = new Expression(expression, localArgs).eval();
    return result.getArgument();
  }

  @Override
  public int getNumArgs() {
    return localVariables.length;
  }

}
