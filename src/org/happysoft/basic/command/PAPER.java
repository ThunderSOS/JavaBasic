/**
 * Copyright 2013. All rights reserved. 
 */
package org.happysoft.basic.command;

import org.happysoft.basic.Program;
import org.happysoft.basic.Statement;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Expression;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class PAPER extends AbstractCommand {

  @Override
  public StructureElement[] getCommandStructure() {
    return new StructureElement[] { StructureElement.EXPRESSION_LIST };
  }  
  
  @Override
  public void execute(Program context, Statement statement) throws SyntaxError {  
    Expression[] expressions = statement.getExpressions();
    if (expressions.length != 3) {
      throw new SyntaxError("PAPER requires 3 arguments");
    }
    int r = (int)expressions[0].eval().getArgument().getIntValue();
    int g = (int)expressions[1].eval().getArgument().getIntValue();
    int b = (int)expressions[2].eval().getArgument().getIntValue();
    context.getDisplayWindow().setPaper(r, g, b);
  }

}
