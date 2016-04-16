
package org.happysoft.basic.expression.function;

import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.TokenType;

/**
 * @author Chris
 */
public class LEN extends Function {

	public int getNumArgs() {
		return 1;
	}

	public Argument eval(Argument... args) throws SyntaxError {
    Argument arg = args[0];
    if (arg.getTokenType() == TokenType.STRING) {
      int d = arg.getStringValue().length();
      Argument a = new Argument("" + d, TokenType.NUMBER);
      return a;
    } else {
      throw new SyntaxError("String argument required");
    }
	}
}