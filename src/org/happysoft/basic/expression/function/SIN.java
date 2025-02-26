package org.happysoft.basic.expression.function;

import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.TokenType;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class SIN extends Function {

  public int getNumArgs() {
    return 1;
  }

  public Argument eval(Argument... args) throws SyntaxError {
    double d = Math.sin(args[0].getDoubleValue());
    Argument a = new Argument("" + d, TokenType.NUMBER);
    return a;
  }
}
