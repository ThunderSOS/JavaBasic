
package org.happysoft.basic.expression;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class BooleanExpressionResult extends ExpressionResult {

	public BooleanExpressionResult(double value) {
		super(value);
		resultType = ResultType.BOOLEAN;
	}
}
