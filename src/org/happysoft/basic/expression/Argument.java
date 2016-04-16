
package org.happysoft.basic.expression;

import org.happysoft.basic.SyntaxError;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class Argument {
	
	protected TokenType argType;
	protected String value;

	public Argument(String value, TokenType type) {
		this.value = value;
		this.argType = type;
	}
  
  public long getIntValue() throws SyntaxError {
    if (argType == TokenType.STRING) {
      throw new SyntaxError("Number required but got a string");
    }
    return (long) getDoubleValue();
  }
  
  public double getDoubleValue() throws SyntaxError {
    if (argType == TokenType.STRING) {
      throw new SyntaxError("Number required but got a string");
    }
    return Double.valueOf(value);
  }

	public String getStringValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public TokenType getTokenType() {
		return argType;
	}

	@Override
	public String toString() {
		return value + "[" + argType + "]";
	}

}
