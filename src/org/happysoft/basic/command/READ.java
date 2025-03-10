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
 * READ data from a DATA statement
 * e.g.
 * READ a,b,c,d,e,f,g
 * DATA 1,2,3,4,5,6,7
 * 
 * To read into an array use: 
 * READ a
 * LET a[n] = a
 * 
 * READ FROM reads from an IO stream opened with OPEN
 * e.g. READ FROM a, a$; start, length
 * will read into a$ start to start+length bytes from 'a'.
 * 
 * READ FROM a, a; start, length
 * will read the bytes an array a instead
 */
public class READ extends AbstractCommand {

  @Override
  public StructureElement[] getCommandStructure() {
    if(extendedMode == ExtendedMode.FROM) {
      return new StructureElement[]{StructureElement.EXPRESSION_LIST};
    }
    return new StructureElement[]{StructureElement.IDENTIFIER_LIST};    
  }

  @Override
  public ExtendedMode[] getExtendedModes() {
    return new ExtendedMode[]{
      ExtendedMode.FROM,
      ExtendedMode.KEY // not implemented yet... 
    };
  }

  @Override
  public void execute(Program context, Statement statement) throws SyntaxError {
    if(ExtendedMode.FROM == extendedMode) {
      readIO(statement);      
    } else {
      readData(context, statement);
    }
  }

  private void readIO(Statement statement) throws SyntaxError {
   
    String ioIdentifier = statement.getIdentifiers()[0];    
    String var = statement.getIdentifiers()[1];
    
    Expression fromE = statement.getExpressions()[0];
    int from = (int) fromE.eval().getArgument().getIntValue();
   
    Expression toE = statement.getExpressions()[1];
    int to = (int) toE.eval().getArgument().getIntValue();
    
    ConnectableTable ct = ConnectableTable.getInstance();
    byte[] b = ct.read(ioIdentifier, from, to);

    if (var.endsWith("$")) {
      String s = new String(b);
      StringTable st = StringTable.getInstance();
      st.setStringVariable(var, s);
    } else {
      NumericArrayTable nt = NumericArrayTable.getInstance();
      nt.createArray(var, b.length);
      nt.setValue(var, b, 0);
    }
  }

  /**
   * Implements the READ command when associated with DATA. DATA lines in the
   * program need to be scanned for and are processed as required.
   *
   * @param context
   * @param statement
   * @throws SyntaxError
   */
  private void readData(Program context, Statement statement) throws SyntaxError {
    System.out.println("Read data");
    int currentLine = context.getCurrentLine();
    int currentStatement = context.getCurrentStatement();
    int dataLine = context.getDataLineNumber();
    int dataStatement = context.getDataStatementNumber();

    DataCache data = DataCache.getInstance();
    String[] identifiers = statement.getIdentifiers();

    for (String var : identifiers) {
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
      
      if (arg.getTokenType() == TokenType.NUMBER) {
        if (var.endsWith("$")) {
          throw new SyntaxError("Numeric assignment to string variable");
        }
        double value = arg.getDoubleValue();
        VariableTable.getInstance().setVariable(var, value);
      }

      if (arg.getTokenType() == TokenType.STRING) {
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
