/**
 * Copyright 2013. All rights reserved. 
 */
package org.happysoft.basic.command;

import org.happysoft.basic.Program;
import org.happysoft.basic.Statement;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Expression;
import org.happysoft.basic.expression.ExpressionResult;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class DATA extends AbstractCommand {
  
  DataCache data = DataCache.getInstance();

  @Override
  public StructureElement[] getCommandStructure() {
    return new StructureElement[] { StructureElement.EXPRESSION_LIST };
  }  
  
  @Override
  public void execute(Program context, Statement statement) throws SyntaxError {
    // only actually read the data when scanning for it, DATA statements are
    // ignored during general execution.
    if(context.readDataFlag) {
      Expression[] expressions = statement.getExpressions();
      for(Expression e : expressions) {
        ExpressionResult result = e.eval();
        data.add(result);
      }
    }
  }
}
