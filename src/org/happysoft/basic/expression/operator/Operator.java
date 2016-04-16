
package org.happysoft.basic.expression.operator;

import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.ExpressionResult;
import org.happysoft.basic.expression.TokenType;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public abstract class Operator {

	protected String symbol;

	public abstract ExpressionResult eval(Argument... args) throws SyntaxError;

	public abstract int getPrecedence();

	public boolean isBoolean() {
		return false;
	}
  
  protected boolean atLeastOneOfType(TokenType tt, Argument... args) {
    for(Argument a: args) {
      if (a.getTokenType() == tt) {
        return true;
      }
    }
    return false;
  }

	@Override
	public String toString() {
		return "" + symbol;
	}
  
}