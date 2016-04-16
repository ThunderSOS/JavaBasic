
package org.happysoft.basic.expression.function;

import java.awt.Toolkit;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.TokenType;

/**
 *
 * @author Chris
 */
public class S_HEIGHT extends Function {

	public int getNumArgs() {
		return 0;
	}

	public Argument eval(Argument... args) throws SyntaxError {
    int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		Argument a = new Argument("" + height, TokenType.NUMBER);
    return a;
	}

}