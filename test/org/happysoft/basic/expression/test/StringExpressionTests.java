
package org.happysoft.basic.expression.test;

import org.happysoft.basic.expression.ExpressionResult;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Expression;
import org.happysoft.basic.var.VariableTable;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chris
 */
public class StringExpressionTests {

  public StringExpressionTests() {
  }

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
    VariableTable vt = VariableTable.getInstance();
    vt.setVariable("t", "1");
    vt.setVariable("t1", "2");
	}

	@After
	public void tearDown() {
	}

  @Test
	public void testSimpleString() {
		String exp = "\"string\"";
		try {
			Expression e = new Expression(exp);
			ExpressionResult result = e.eval();
			String d = result.getArgument().getStringValue();
			assertEquals(d, "string");
		} catch (SyntaxError se) {
			fail(se.getMessage());
		}
	}
  
  @Test
	public void testSimpleStringConcat() {
		String exp = "\"string\"+ \"1\"";
		try {
			Expression e = new Expression(exp);
			ExpressionResult result = e.eval();
			String d = result.getArgument().getStringValue();
			assertEquals(d, "string1");
		} catch (SyntaxError se) {
			fail(se.getMessage());
		}
	}	
  
 @Test
	public void testMultiStringConcat() {
		String exp = "\"t=\" + int(t) + \", t1=\" + int(t1)\n";
		try {
			Expression e = new Expression(exp);
			ExpressionResult result = e.eval();
			String d = result.getArgument().getStringValue();
			assertEquals(d, "t=1, t1=2");
      
		} catch (SyntaxError se) {
			fail(se.getMessage());
		}
	}	

}