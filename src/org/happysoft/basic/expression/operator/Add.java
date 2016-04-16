
package org.happysoft.basic.expression.operator;

import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.ExpressionResult;
import org.happysoft.basic.expression.TokenType;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class Add extends Operator {

	public Add() {
		symbol = "+";
	}
	
	public int getPrecedence() {
		return 11;
	}

	public ExpressionResult eval(Argument... args) throws SyntaxError {
    if(atLeastOneOfType(TokenType.STRING, args)) {
      return new ExpressionResult(args[0].getStringValue() + args[1].getStringValue());
    }
		return new ExpressionResult(args[0].getDoubleValue() + args[1].getDoubleValue());
	}
}
