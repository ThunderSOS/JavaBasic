/**
 * Copyright 2013. All rights reserved. 
 */
package org.happysoft.basic.command;

import org.happysoft.basic.Program;
import org.happysoft.basic.Statement;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.var.ConnectableTable;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class CLOSE extends AbstractCommand {

  @Override
  public StructureElement[] getCommandStructure() {
    return new StructureElement[] { 
      StructureElement.IDENTIFIER 
    };
  } 

  @Override
  public void execute(Program context, Statement statement) throws SyntaxError {
    String identifier = statement.getIdentifiers()[0];
    ConnectableTable table = ConnectableTable.getInstance();
    table.close(identifier);
  }
 
}
