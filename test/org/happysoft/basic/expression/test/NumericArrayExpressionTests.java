
package org.happysoft.basic.expression.test;

import java.util.HashMap;
import java.util.Map;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Expression;
import org.happysoft.basic.expression.ExpressionResult;
import org.happysoft.basic.var.NumericArrayTable;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Chris
 */
public class NumericArrayExpressionTests {
  
  private Map<String, TestSet> tests = new HashMap<String, TestSet>();
  
  private class TestSet {
    
    String expression;
    double expectedResult;
    
    public TestSet(String expression, double result) {
      this.expectedResult = result;
      this.expression = expression;
    } 
  }

  public NumericArrayExpressionTests() {
  }

	@Before
	public void setUp() throws SyntaxError{
    NumericArrayTable n = NumericArrayTable.getInstance();
    n.createArray("a", 2, 2);
    n.setValue("a", 0, 0, 0);
    n.setValue("a", 3, 0, 1);
    n.setValue("a", 5, 1, 0);
    n.setValue("a", 7, 1, 1);
    tests.put("test1", new TestSet("a[0, 0]", 0d));
    tests.put("test2", new TestSet("a[0, 1]", 3d));
    tests.put("test3", new TestSet("a[1, 0]", 5d));
    tests.put("test4", new TestSet("a[1, 1]", 7d));
    tests.put("testNestedArray1", new TestSet("a[a[0, 0]+1, a[0, 0]+1]", 7d));
	}

	@Test
	public void testExpressions() {
		for(String name : tests.keySet()) {
      TestSet ts = tests.get(name);
      try {
        Expression e = new Expression(ts.expression);
        ExpressionResult result = e.eval();
        double d = result.getArgument().getDoubleValue();
        if(d != ts.expectedResult) {
          fail("Test '" + name + "' did not evaluate correctly; got: " + d + " but expected: " + ts.expectedResult);
        }
      } catch (SyntaxError se) {
        fail("Test " + name + " failed with message: " + se.getMessage());
      }
    }
	}
}
