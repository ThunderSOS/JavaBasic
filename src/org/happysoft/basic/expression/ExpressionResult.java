
package org.happysoft.basic.expression;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class ExpressionResult {
	
	private Argument result;
	protected ResultType resultType;

	public enum ResultType {
		BOOLEAN,
		NUMERIC,
		STRING
	}

	protected ExpressionResult() {
	}

	public ExpressionResult(String value) {
		this.result = new Argument(value, TokenType.STRING);
		resultType = ResultType.STRING;
	}

	public ExpressionResult(double value) {
		this.result = new Argument("" + value, TokenType.NUMBER);
		resultType = ResultType.NUMERIC;
	}

	public ExpressionResult(Argument value) {
		this.result = value;
    switch(value.getTokenType()) {
      case STRING:
        resultType = ResultType.STRING;
        value.argType = TokenType.STRING;
        break;
        
      default:
        resultType = ResultType.NUMERIC;
        value.argType = TokenType.NUMBER;
        break;
    }
	}
  
  public Argument getArgument() {
    return result;
  }

	public ResultType getResultType() {
		return resultType;
	}

	public void setResultType(ResultType type) {
		this.resultType = type;
	}

	@Override
	public String toString() {
		return "" + result.getStringValue() + "[" + resultType + "]";
	}

}
