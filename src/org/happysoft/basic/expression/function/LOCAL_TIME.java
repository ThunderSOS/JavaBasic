
package org.happysoft.basic.expression.function;

import java.util.Calendar;
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
		long time = Calendar.getInstance().getTimeInMillis();
    Argument a = new Argument("" + time, TokenType.NUMBER);
    return a;
	}
}
