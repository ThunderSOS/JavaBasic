
package org.happysoft.basic.expression.operator;

import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.BooleanExpressionResult;
import org.happysoft.basic.expression.TokenType;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class BooleanEquals extends Operator {

	public BooleanEquals() {
		symbol = "==";
	}

	@Override
	public boolean isBoolean() {
		return true;
	}

	public int getPrecedence() {
		return 8;
	}

	public BooleanExpressionResult eval(Argument... args) throws SyntaxError {
    if(atLeastOneOfType(TokenType.STRING, args)) {
      return new BooleanExpressionResult( (args[0].getStringValue().equals(args[1].getStringValue())) ? 1 : 0);
    }
		return new BooleanExpressionResult( (args[0].getDoubleValue() == args[1].getDoubleValue() ) ? 1 : 0);
	}
}