
package org.happysoft.basic.expression.operator;

import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.BooleanExpressionResult;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class BooleanOr extends Operator {

	public BooleanOr() {
		symbol = "||";
	}

	@Override
	public boolean isBoolean() {
		return true;
	}

	public int getPrecedence() {
		return 4;
	}

	public BooleanExpressionResult eval(Argument... args) throws SyntaxError {
		return new BooleanExpressionResult((args[0].getIntValue() == 1 || args[1].getIntValue() == 1) ? 1 : 0);
	}
}