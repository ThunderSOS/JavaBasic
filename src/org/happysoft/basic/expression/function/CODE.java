/**
 * Copyright 2013. All rights reserved.
 */
package org.happysoft.basic.expression.function;

import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.TokenType;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class CODE extends Function {

  @Override
  public Argument eval(Argument... args) throws SyntaxError {
    char c = (args[0].getStringValue().charAt(0));
    Argument a = new Argument("" + (int) c, TokenType.NUMBER);
    return a;
  }

  @Override
  public int getNumArgs() {
    return 1;
  }

}
