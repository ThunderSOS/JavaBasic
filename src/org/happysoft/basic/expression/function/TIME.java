
package org.happysoft.basic.expression.function;

import java.util.Calendar;
import java.util.TimeZone;
import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.TokenType;

/**
 * @author Chris
 */
public class TIME extends Function {

	public int getNumArgs() {
		return 0;
	}

	public Argument eval(Argument... args) {
		long time = System.currentTimeMillis();
    Argument a = new Argument("" + time, TokenType.NUMBER);
    return a;
	}
}