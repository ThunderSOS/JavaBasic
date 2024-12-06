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
public class SUBSTRING extends Function {

  public int getNumArgs() {
    return 3;
  }

  public Argument eval(Argument... args) throws SyntaxError {
    Argument arg = args[0];
    switch (arg.getTokenType()) {
      case STRING:
        String string = arg.getStringValue();
        Argument start = args[1];
        Argument end = args[2];
        if (!(start.getTokenType() == TokenType.NUMBER || end.getTokenType() == TokenType.NUMBER)) {
          throw new SyntaxError("String and two numeric arguments required for substring");
        }
        Argument a = new Argument(string.substring((int) start.getIntValue(), (int) end.getIntValue()), TokenType.STRING);
        return a;
        
      case ARRAY:
        return null;

      default:
        throw new SyntaxError("String argument required");
    }
  }
}
