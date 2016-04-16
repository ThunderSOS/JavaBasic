
package org.happysoft.basic.expression.operator;

import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.ExpressionResult;

/**
 * @author Chris
 */
public class Power extends Operator {

	public Power() {
		symbol = "**";
	}
	
	public int getPrecedence() {
		return 10;
	}

	public ExpressionResult eval(Argument... args) throws SyntaxError {
    Double base = args[0].getDoubleValue();
    Double exp = args[1].getDoubleValue();
    double result = Math.pow(base, exp);
		return new ExpressionResult(result);
	}
}