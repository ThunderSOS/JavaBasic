
package org.happysoft.basic.expression.function;

import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.TokenType;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class RND extends Function {

	public int getNumArgs() {
		return 0;
	}

	public Argument eval(Argument... args) {
		double d = Math.random();
    Argument a = new Argument("" + d, TokenType.NUMBER);
    return a;
	}
}
