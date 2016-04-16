
package org.happysoft.basic.expression.function;

import java.awt.Toolkit;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.TokenType;

/**
 * @author Chris
 */
public class S_WIDTH extends Function {

	public int getNumArgs() {
		return 0;
	}

	public Argument eval(Argument... args) throws SyntaxError {
    int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		Argument a = new Argument("" + width, TokenType.NUMBER);
    return a;
	}

}