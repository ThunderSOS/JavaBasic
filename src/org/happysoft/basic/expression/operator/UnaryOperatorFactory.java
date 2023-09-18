package org.happysoft.basic.expression.operator;

import org.happysoft.basic.SyntaxError;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class UnaryOperatorFactory {

  private static final UnaryOperatorFactory instance = new UnaryOperatorFactory();;

  private UnaryOperatorFactory() {
  }

  public static UnaryOperatorFactory getInstance() {
    return instance;
  }

  public Operator getOperator(String operator) throws SyntaxError {
    if ("-".equals(operator)) {
      return new UnaryMinus();
    }
    if ("+".equals(operator)) {
      return new UnaryPlus();
    }
    if ("!".equals(operator)) {
      return new BooleanNot();
    }
    throw new SyntaxError("Unknown unary operator: " + operator);
  }
}
