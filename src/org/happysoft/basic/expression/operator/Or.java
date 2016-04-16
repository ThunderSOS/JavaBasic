
package org.happysoft.basic.expression.operator;

import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.ExpressionResult;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class Or extends Operator {

	public Or() {
		symbol = "|";
	}

	public int getPrecedence() {
		return 5;
	}

	public ExpressionResult eval(Argument... args) throws SyntaxError {
		return new ExpressionResult(args[0].getIntValue() | args[1].getIntValue());
	}
}