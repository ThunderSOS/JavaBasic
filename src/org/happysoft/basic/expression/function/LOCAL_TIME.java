
package org.happysoft.basic.expression.function;

import java.util.Date;
import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.TokenType;

/**
 * @author Chris
 */
public class LOCAL_TIME extends Function {

	public int getNumArgs() {
		return 0;
	}

	public Argument eval(Argument... args) {
		long time = new Date().getTime();
    Argument a = new Argument("" + time, TokenType.NUMBER);
    System.out.println("time from arg: " + a.getStringValue());
    return a;
	}
}
