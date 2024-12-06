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
public class STR$ extends Function {

  public Argument eval(Argument... args) throws SyntaxError {
    double d = args[0].getDoubleValue();
    Argument a = new Argument("" + d, TokenType.STRING);
    return a;
  }

  public int getNumArgs() {
    return 1;
  }
}
