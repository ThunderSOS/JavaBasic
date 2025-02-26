package org.happysoft.basic.expression.function;

import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Argument;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public abstract class Function {

  public Function() {
  }

  public abstract Argument eval(Argument... args) throws SyntaxError;

  public abstract int getNumArgs();
}
