/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.happysoft.basic.expression.test;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author chris
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
  SimpleNumericExpressionTests.class, 
  StringExpressionTests.class,
  FunctionExpressionTests.class,
  NumericArrayExpressionTests.class,
})
public class ExpressionTestSuite {

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	

}