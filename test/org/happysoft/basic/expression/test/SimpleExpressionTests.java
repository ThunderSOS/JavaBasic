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
public class SimpleExpressionTests {

  private Map<String, TestSet> tests = new HashMap<String, TestSet>();

  private class TestSet {

    String expression;
    double expectedResult;

    public TestSet(String expression, double result) {
      this.expectedResult = result;
      this.expression = expression;
    }
  }

  public SimpleExpressionTests() {
  }

  @Before
  public void setUp() {
    tests.put("testDeeplyHested", new TestSet("(((((2)+1))))", 3d));
    tests.put("testNestedParenthesis", new TestSet("1+((2+5)*2)", 15d));
    tests.put("testParenthesisPrecedence", new TestSet("(3+2)*4", 20d));
    tests.put("testMultiplyPrecedence", new TestSet("3+2*4", 11d));
    tests.put("testDoubleNegative", new TestSet("1--1", 2d));
    tests.put("testSimpleUnary1", new TestSet("-(-1)", 1d));
    tests.put("testSimpleUnary2", new TestSet("-1", -1d));
    tests.put("testSimpleUnary3", new TestSet("2++1", 3d));
    tests.put("testSimpleUnary4", new TestSet("2+-1", 1d));
    tests.put("testPowers", new TestSet("2**2**3", 64d));
    tests.put("testPower", new TestSet("2+1**4", 81d)); // power precedence 
    tests.put("testPower2", new TestSet("1+(2**4)", 17d));
    tests.put("testBooleanAnd", new TestSet("2 & 3", 2d));
    tests.put("testBooleanOr", new TestSet("1 | 2 | 4", 7d));
    tests.put("testBooleanNot", new TestSet("!1", 0d));
    tests.put("testMultiplication", new TestSet("6/2*(1+2)", 9d));
  }

  @Test
  public void testExpressions() {
    for (String name : tests.keySet()) {
      TestSet ts = tests.get(name);
      try {
        Expression e = new Expression(ts.expression);
        ExpressionResult result = e.eval();
        double d = result.getArgument().getDoubleValue();
        if (d != ts.expectedResult) {
          fail("Test '" + name + "' did not evaluate correctly; got: " + d + " but expected: " + ts.expectedResult);
        }
      } catch (SyntaxError se) {
        fail("Test " + name + " failed with message: " + se.getMessage());
      }
    }
  }

}
