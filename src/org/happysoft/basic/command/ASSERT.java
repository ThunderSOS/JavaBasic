/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.happysoft.basic.command;

import org.happysoft.basic.Program;
import org.happysoft.basic.Statement;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.ExpressionResult;

/**
 * @author Chris
 */
public class ASSERT extends AbstractCommand {

  @Override
  public StructureElement[] getCommandStructure() {
    return new StructureElement[] { 
      StructureElement.EXPRESSION
    };
  }
  
  @Override
  public void execute(Program context, Statement statement) throws SyntaxError {
    ExpressionResult result = statement.getExpressions()[0].eval();
    if (!(result.getResultType() == ExpressionResult.ResultType.BOOLEAN)) {
      throw new SyntaxError("Boolean expression required");
    }
    if (result.getArgument().getIntValue() != 1) {
      System.out.println("Assertion failure: " + statement.getExpressions()[0]);
      context.stop();
    }
  }

}