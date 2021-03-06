
package org.happysoft.basic.expression.operator;

import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.BooleanExpressionResult;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class GreaterThanEquals extends Operator {

	public GreaterThanEquals() {
		symbol = ">=";
	}

	@Override
	public boolean isBoolean() {
		return true;
	}

	public int getPrecedence() {
		return 9;
	}

	public BooleanExpressionResult eval(Argument... args) throws SyntaxError {
		return new BooleanExpressionResult((args[0].getDoubleValue() >= args[1].getDoubleValue()) ? 1 : 0);
	}
}