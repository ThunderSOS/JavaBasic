
package org.happysoft.basic.expression;

import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.var.NumericArrayTable;
import org.happysoft.basic.var.StringArrayTable;
import org.happysoft.basic.var.StringTable;
import org.happysoft.basic.var.VariableTable;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class ArgumentFactory {

	private static ArgumentFactory instance;

	static {
		instance = new ArgumentFactory();
	}

	private ArgumentFactory() {
	}

	public static ArgumentFactory getInstance() {
		return instance;
	}

	public Argument getArgument(String value, TokenType argType, int[] dimensions, LocalArgumentMap localArgs) throws SyntaxError {
		switch(argType) {
			case NUMBER:
      case STRING:
				return new Argument(value, argType);

			case VARIABLE:
        if(localArgs != null && localArgs.hasKey(value)) {
          return localArgs.getKey(value);
        }
        if (value.endsWith("$")) {
          String val = StringTable.getInstance().getVariable(value);
          return new Argument(val, TokenType.STRING);
        }
				double val = VariableTable.getInstance().getVariable(value);
				return new Argument("" + val, TokenType.NUMBER);
        
      case ARRAY:
        if (localArgs != null && localArgs.hasKey(value)) {
          return localArgs.getKey(value);
        }
        if (value.endsWith("$")) {
          String s = StringArrayTable.getInstance().getValue(value, dimensions);
          return new Argument(s, TokenType.STRING);
        } else {
          double d = NumericArrayTable.getInstance().getValue(value, dimensions);
          return new Argument("" + d, TokenType.NUMBER);
        }
		}
		throw new UnsupportedOperationException("Unsupported arg type: " + argType + "[" + value + "]");
	}
}
