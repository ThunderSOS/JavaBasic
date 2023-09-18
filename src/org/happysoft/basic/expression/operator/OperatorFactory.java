package org.happysoft.basic.expression.operator;

import org.happysoft.basic.SyntaxError;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class OperatorFactory {

  private static OperatorFactory instance;

  static {
    instance = new OperatorFactory();
  }

  private OperatorFactory() {
  }

  public static OperatorFactory getInstance() {
    return instance;
  }

  public Operator getOperator(String operator) throws SyntaxError {
    Operator o = Operators.getBySymbol(operator);
    if (o == null) {
      throw new SyntaxError("Unknown operator: " + operator);
    }
    return o;
  }
}
