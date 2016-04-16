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
 * 
 */
public class RESTORE extends AbstractCommand {
  
  DataCache dataCache = DataCache.getInstance();

  @Override
  public StructureElement[] getCommandStructure() {
    return new StructureElement[] { StructureElement.EXPRESSION_LIST };
  }  
  
  @Override
  public void execute(Program context, Statement statement) throws SyntaxError {
    Expression e = statement.getExpressions()[0];
    ExpressionResult result = e.eval();
    context.setDataLineNumber((int)result.getArgument().getIntValue());
    context.setDataStatementNumber(0);
    dataCache.reset();
    
  }
}
