
package org.happysoft.basic.command;

import org.happysoft.basic.Program;
import org.happysoft.basic.Statement;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Expression;
import org.happysoft.basic.var.NumericArrayTable;
import org.happysoft.basic.var.StringArrayTable;

/**
 * @author Chris
 * DIMension an array. 
 * If the array name ends with a $ then its an array of string types otherwise
 * its an array of numbers. 
 */
public class DIM extends AbstractCommand {

  @Override
  public StructureElement[] getCommandStructure() {
    return new StructureElement[] { StructureElement.IDENTIFIER, StructureElement.ARRAY_LIST };
  }

  @Override
  public void execute(Program context, Statement statement) throws SyntaxError {
    String arrayName = statement.getIdentifiers()[0];
    Expression[] indexExpressions = statement.getExpressions();
    int[] dimensions = new int[indexExpressions.length];
    
    for(int i = 0; i < dimensions.length; i++) {
      dimensions[i] = (int)indexExpressions[i].eval().getArgument().getIntValue();
    }
    
    if (arrayName.endsWith("$")) {
      StringArrayTable.getInstance().createArray(arrayName, dimensions);
    
    } else {
      NumericArrayTable.getInstance().createArray(arrayName, dimensions);
    }
  
  }
  
}
