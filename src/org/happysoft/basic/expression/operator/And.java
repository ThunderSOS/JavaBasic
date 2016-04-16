
package org.happysoft.basic.expression.operator;

import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.ExpressionResult;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class And extends Operator {

	public And() {
		symbol = "&";
	}

	public int getPrecedence() {
		return 7;
	}

	public ExpressionResult eval(Argument... args) throws SyntaxError {
		return new ExpressionResult(args[0].getIntValue() & args[1].getIntValue());
	}
}