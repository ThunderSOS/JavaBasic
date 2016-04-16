/*
 * 
 * 
 */
package org.happysoft.basic.command;

import org.happysoft.basic.Program;
import org.happysoft.basic.Statement;
import org.happysoft.basic.SyntaxError;

/**
 *
 * @author Chris
 */
public class CLS extends AbstractCommand {
  
  @Override
  public StructureElement[] getCommandStructure() {
    return new StructureElement[] { };
  }
  
  @Override
  public void execute(Program context, Statement statement) throws SyntaxError {
    context.getDisplayWindow().cls();
  }
}
