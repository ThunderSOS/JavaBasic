
package org.happysoft.basic.command;

import org.happysoft.basic.expression.Expression;
import org.happysoft.basic.expression.ExpressionResult;

import org.happysoft.basic.Program;
import org.happysoft.basic.Statement;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.var.Connectable;
import org.happysoft.basic.var.ConnectableTable;
import org.happysoft.basic.var.FileConnectable;
import org.happysoft.basic.var.HttpConnectable;
import org.happysoft.basic.var.TcpConnectable;

/**
 * OPEN a connection to somewhere (file, http, etc). 
 * OPEN HTTP a; "www.google.com", 80
 * OPEN FILE f; "file.txt"
 * a is the connection name for use in future READ/WRITE commands. 
 * 
 * @author Chris
 */
public class OPEN extends AbstractCommand {

  @Override
  public StructureElement[] getCommandStructure() {
    return new StructureElement[] { 
      StructureElement.EXPRESSION_LIST 
    };
  }
  
  @Override
  public ExtendedMode[] getExtendedModes() {
    return new ExtendedMode[] {
      ExtendedMode.TCP, 
      ExtendedMode.UDP, 
      ExtendedMode.HTTP, 
      ExtendedMode.FILE
    };
  }

  @Override
  public void execute(Program context, Statement statement) throws SyntaxError {
    switch(extendedMode) {
      case FILE:
        doOpenFile(statement);
        break;
        
      case TCP: 
        doTcp(statement);
        break;
        
      case HTTP:
        doHttp(statement);
        break;
    }
    
  }
  
  private void doOpenFile(Statement statement) throws SyntaxError {
    Expression[] expressions = getExpressions(statement);
    String identifier = statement.getIdentifiers()[0];
    ExpressionResult filenameExp = expressions[0].eval();
    ExpressionResult porte = expressions[1].eval();
    String filename = filenameExp.getArgument().getStringValue();
    int append = (int)porte.getArgument().getIntValue();
    Connectable tcp = new FileConnectable(filename, append == 1);
    tcp.connect();
    ConnectableTable table = ConnectableTable.getInstance();
    table.add(identifier, tcp); 
  }
  
  private void doHttp(Statement statement) throws SyntaxError {    
    Expression[] expressions = getExpressions(statement);
    String identifier = statement.getIdentifiers()[0];
    ExpressionResult hoste = expressions[0].eval();
    ExpressionResult porte = expressions[1].eval();
    String host = hoste.getArgument().getStringValue();
    int port = (int)porte.getArgument().getIntValue();
    Connectable tcp = new HttpConnectable();
    tcp.connect(host, "" + port);
    ConnectableTable table = ConnectableTable.getInstance();
    table.add(identifier, tcp); 
  }
  
  private void doTcp(Statement statement) throws SyntaxError {    
    Expression[] expressions = getExpressions(statement);
    String identifier = statement.getIdentifiers()[0];
    ExpressionResult hoste = expressions[0].eval();
    ExpressionResult porte = expressions[1].eval();
    String host = hoste.getArgument().getStringValue();
    int port = (int)porte.getArgument().getIntValue();
    Connectable tcp = new TcpConnectable();
    tcp.connect(host, "" + port);
    ConnectableTable table = ConnectableTable.getInstance();
    table.add(identifier, tcp); 
  }
  
  private Expression[] getExpressions(Statement statement) throws SyntaxError {
    Expression[] expressions = statement.getExpressions();
    if(expressions.length != 2) {
      throw new SyntaxError("2 arguments required for OPEN");
    }
    return expressions;
  }

}