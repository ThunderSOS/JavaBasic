
package org.happysoft.basic.expression.test;

import java.util.HashMap;
import java.util.Map;
import org.happysoft.basic.expression.ExpressionResult;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Expression;
import org.happysoft.basic.var.VariableTable;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chris
 */
public class StringExpressionTests {
  
  private Map<String, TestSet> tests = new HashMap<String, TestSet>();
  
  private class TestSet {
    
    String expression;
    String expectedResult;
    
    public TestSet(String expression, String result) {
      this.expectedResult = result;
      this.expression = expression;
    }
  }

  public StringExpressionTests() {
  }

	@Before
	public void setUp() {
    VariableTable vt = VariableTable.getInstance();
    vt.setVariable("t", "1");
    vt.setVariable("t1", "2");
    
    tests.put("Test quoted string eval", new TestSet("\"string\"", "string"));
    tests.put("Test simple concatenation", new TestSet("\"string\"+ \"1\"", "string1"));
    tests.put("Test MultiString concat", new TestSet("\"t=\" + int(t) + \", t1=\" + int(t1)\n", "t=1, t1=2"));
    tests.put("Test str$", new TestSet("str$(1)", "1.0"));
	}
  
  @Test
  public void testExpressions() {
    for(String name : tests.keySet()) {
      TestSet ts = tests.get(name);
      try {
        Expression e = new Expression(ts.expression);
        ExpressionResult result = e.eval();
        String d = result.getArgument().getStringValue();
        if(!d.equals(ts.expectedResult)) {
          fail("Test '" + name + "' did not evaluate correctly; got: " + d + " but expected: " + ts.expectedResult);
        }
      } catch (SyntaxError se) {
        fail("Test " + name + " failed with message: " + se.getMessage());
      }
    }
  }
  
}