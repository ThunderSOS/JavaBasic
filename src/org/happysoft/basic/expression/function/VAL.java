/**
 * Copyright 2013. All rights reserved. 
 */
package org.happysoft.basic.expression.function;

import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.Expression;
import org.happysoft.basic.expression.TokenType;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class VAL extends Function {
    
	public Argument eval(Argument... args) throws SyntaxError {
    String val = args[0].getStringValue();
    Expression e = new Expression(val);
    Double d = e.eval().getArgument().getDoubleValue();
		Argument a = new Argument("" + d, TokenType.NUMBER);
    return a;
  }
          
	public int getNumArgs() {
    return 1;
  }
}
