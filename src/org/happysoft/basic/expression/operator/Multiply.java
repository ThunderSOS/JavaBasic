
package org.happysoft.basic.expression.operator;

import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.ExpressionResult;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class Multiply extends Operator {

	public Multiply() {
		symbol = "*";
	}

	public int getPrecedence() {
		return 12;
	}

	@Override
	public ExpressionResult eval(Argument... args) throws SyntaxError {
		return new ExpressionResult(args[0].getDoubleValue()*args[1].getDoubleValue());
	}

}
