
package org.happysoft.basic.expression.test;

import java.util.HashMap;
import java.util.Map;
import org.happysoft.basic.expression.ExpressionResult;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Expression;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author chris
 */
public class BooleanExpressionTests {
 
  private Map<String, TestSet> tests = new HashMap<String, TestSet>();
  
  private class TestSet {
    
    String expression;
    boolean expectedResult;
    
    public TestSet(String expression, boolean result) {
      this.expectedResult = result;
      this.expression = expression;
    } 
  }

  public BooleanExpressionTests() {
  }

	@Before
	public void setUp() {    
    tests.put("testBoolean1", new TestSet("3 < 2", false));
    tests.put("testBoolean2", new TestSet("2 < 3", true));
    tests.put("testBoolean3", new TestSet("3 && 2", false));
    tests.put("testBoolean4", new TestSet("2 && 3", false));
	}

	@Test
	public void testExpressions() {
		for(String name : tests.keySet()) {
      TestSet ts = tests.get(name);
      try {
        Expression e = new Expression(ts.expression);
        ExpressionResult result = e.eval();
        System.out.println("ResultType: " +  result.getResultType());
        System.out.println("Result: " + result.getArgument().getStringValue());
        if(result.getResultType() == ExpressionResult.ResultType.BOOLEAN) {
          
        }
        long value = result.getArgument().getIntValue();
        boolean b = value == 1;
        
        if(b != ts.expectedResult) {
          fail("Test '" + name + "' did not evaluate correctly; got: " + b + " but expected: " + ts.expectedResult);
        }
      } catch (SyntaxError se) {
        fail("Test " + name + " failed with message: " + se.getMessage());
      }
    }
	}

}