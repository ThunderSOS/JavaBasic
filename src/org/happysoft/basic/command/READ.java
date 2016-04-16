/**
 * Copyright 2013. All rights reserved. 
 */
package org.happysoft.basic.command;

import org.happysoft.basic.Program;
import org.happysoft.basic.Statement;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.Expression;
import org.happysoft.basic.expression.ExpressionResult;
import org.happysoft.basic.expression.TokenType;
import org.happysoft.basic.var.ConnectableTable;
import org.happysoft.basic.var.NumericArrayTable;
import org.happysoft.basic.var.StringTable;
import org.happysoft.basic.var.VariableTable;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class READ extends AbstractCommand {

  @Override
  public StructureElement[] getCommandStructure() {
    return new StructureElement[] { StructureElement.IDENTIFIER_LIST };
  }  
  
  @Override
  public ExtendedMode[] getExtendedModes() {
    return new ExtendedMode[] {
      ExtendedMode.FROM,
      ExtendedMode.KEY
    };
  }
  
  @Override
  public void execute(Program context, Statement statement) throws SyntaxError {
    switch(extendedMode) {
      case FROM:
        readIO(statement);
        break;
        
      case KEY:
        System.out.println("Read key");
        break;
        
      default:
        readData(context, statement);
    }
  }
  
  private void readIO(Statement statement) throws SyntaxError {
    String ioIdentifier = statement.getIdentifiers()[0];
    String var = statement.getIdentifiers()[1];
    Expression maxE = statement.getExtendedModeExpressions()[0];
    int max = (int) maxE.eval().getArgument().getIntValue();
    ConnectableTable ct = ConnectableTable.getInstance();
    byte[] b = ct.read(ioIdentifier, max);
   
    if(var.endsWith("$")) {
      String s = new String(b);
      StringTable st = StringTable.getInstance();
      st.setStringVariable(var, s);
    } else {
      NumericArrayTable nt = NumericArrayTable.getInstance();
      nt.createArray(var, b.length);
      for(int i = 0; i < b.length; i++) {
        nt.setValue(var, b[i], i);
      }
    }
  }
  
  /**
   * Implements the READ command when associated with DATA. 
   * DATA lines in the program need to be scanned for and are processed 
   * as required. 
   * 
   * @param context
   * @param statement
   * @throws SyntaxError 
   */
  private void readData(Program context, Statement statement) throws SyntaxError {
    int currentLine = context.getCurrentLine();
    int currentStatement = context.getCurrentStatement();
    int dataLine = context.getDataLineNumber(); 
    int dataStatement = context.getDataStatementNumber();

    DataCache data = DataCache.getInstance();
    String[] identifiers = statement.getIdentifiers();

    for(String var : identifiers) {  
      ExpressionResult res = null;
      try {
        res = data.getNext();     
        
        // we may have already read some data but possibly not enough. 
      } catch (IndexOutOfBoundsException ie) {
        // so let's find the next line containing a DATA statement and 
        // execute it. 
        context.scanFor(DATA.class, null, dataLine, dataStatement, true);
        // Now, we should have some data to work with, which will get us through 
        // the next pass but ... 
        try {
          // if we've run out of DATA statements currentLine will be null
          dataLine = context.getCurrentLine();
          dataStatement = context.getCurrentStatement();
          context.setDataLineNumber(dataLine);
          context.setDataStatementNumber(dataStatement);

          res = data.getNext();

        } catch (NullPointerException npe) {

          context.setCurrentLineNumber(currentLine);
          context.setCurrentStatementNumber(currentStatement);
          throw new SyntaxError("Not enough DATA");
        } 
      }

      Argument arg = res.getArgument();

      if(arg.getTokenType() == TokenType.NUMBER) {
        if (var.endsWith("$")) {
          throw new SyntaxError("Numeric assignment to string variable");
        }
        double value = arg.getDoubleValue();
        VariableTable.getInstance().setVariable(var, value);
      }  

      if(arg.getTokenType() == TokenType.STRING) {
        if (!var.endsWith("$")) {
          throw new SyntaxError("String assignment to non string variable");
        }
        String string = arg.getStringValue();
        StringTable.getInstance().setStringVariable(var, string);
      }
    }
    
    // continue from where we were
    context.setLineAndStatement(currentLine, currentStatement);
  }
}
