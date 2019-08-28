
package org.happysoft.basic;

import java.util.ArrayList;

import org.happysoft.basic.command.*;

import org.happysoft.basic.expression.Expression;
import org.happysoft.basic.expression.StringUtils;

/**
 * @author Chris Francis 
 * Represents a 'compiled' form of the BASIC statement. The statement will be parsed 
 * and split into its constituent parts: Command, assignments and expressions and is 
 * ready to be executed. 
 */
public class Statement {
  
  private int line, statementInLine;
  
  private Command command;
  private ArrayList<Expression> expressionList = new ArrayList<Expression>();
  private ArrayList<Expression> extendedModeExpressionList = new ArrayList<Expression>();
  
  private Expression assignmentExpression = null;
  
  private String statement;
  private int currentPosition = 0;
  
  private Statement nextStatement = null;

  private ArrayList<String> identifiers = new ArrayList<String>();
  private ArrayList<String> assignmentVariableList = new ArrayList<String>();
  
  
  public Statement(String statement, int line, int statementNumber) throws SyntaxError {
    this.line = line;
    this.statementInLine = statementNumber;
    this.statement = statement.trim();
    System.out.println("Parsing statement: " + this.statement);
    parse();
  }
  
  private void parse() throws SyntaxError {
    currentPosition = statement.indexOf(' ');
    String commandString;
    if (currentPosition > 0) {
      commandString = statement.substring(0, currentPosition).trim();
    } else {
      commandString = statement;
    }
    command = CommandFactory.getInstance().getCommand(commandString);
    System.out.println("Command: " + command);
    
    command.setExtendedMode(null);
    ExtendedMode[] extendedModes = command.getExtendedModes();
    
    if(extendedModes != null) {    
      System.out.println("Looking for extended modes");
      currentPosition++;
      int end = statement.indexOf(' ', currentPosition);

      if (end > currentPosition) {
        String mode = statement.substring(currentPosition, end).trim();
        System.out.println("Found: " + mode);
        for(ExtendedMode extendedMode : extendedModes) {
          if(extendedMode.getMode().equals(mode)) {
            currentPosition = end+1;
            command.setExtendedMode(extendedMode);
            readExtendedModeArgs(extendedMode.getStructure());
          }
        }
      }
    }
    
    StructureElement[] structure = command.getCommandStructure();
    ConnectorElement[] connectives = command.getExpectedConnectives();
    
    int connectiveIndex = 0;
    int currentPositionCopy = currentPosition;
    
    for (StructureElement se : structure) {
      if (currentPosition > statement.length() || currentPosition < 0) {
        return;
      }
      int nextConnective = -1;
      int connectiveLen = 0;
      String next = statement.substring(currentPosition);
      
      // check for a connective (TO, THEN, etc).
      // if there is one then the next part of the statement is from
      // current position to the beginning of the next connective.
      try {
        ConnectorElement connective = connectives[connectiveIndex++];
        nextConnective = checkForKeyword(connective.name(), currentPosition);
        connectiveLen = connective.name().length();
        
        if (nextConnective < 0) {
          if (connective.isOptional()) {
            continue;
          } else {
            throw new SyntaxError(connective.name() + " expected");
          }
        }
        
      } catch (ArrayIndexOutOfBoundsException ae) {
        // ignore
      }
      
      if (nextConnective > currentPosition) {
        next = statement.substring(currentPosition, nextConnective);
      } else {
        if(currentPosition > statement.length()) {
          return;
        }
      } 
      
      next = next.trim();
      System.out.println("Next: " + next);
      
      switch(se) {
        case IDENTIFIER:
          doIdentifier(next);
          break;
          
        case IDENTIFIER_LIST:
          doIdentifierList(next);
          break;
          
        case FUNCTION_ASSIGNMENT:
          doFunctionAssignment(next);
          break;
          
        case ASSIGNMENT:
          doAssignment(next);
          break;
        
        case EXPRESSION:
          doExpression(next);
          break;
        
        case EXPRESSION_LIST:
          doExpressionList(next);
          break;  
         
        case ARRAY_LIST:
          doArrayList(next);
          break;  
          
        case COMMAND:
          nextStatement = new Statement(next, this.line, this.statementInLine);
          break;          
      }
      
      // reset current position to after the connective if there was one
      if(nextConnective > currentPositionCopy) {
        currentPosition = nextConnective + connectiveLen;
      }
    }
  }
  
  private void readExtendedModeArgs(StructureElement[] structures) throws SyntaxError {
    System.out.println("Read extended mode arguments");
    int semi = statement.indexOf(';', currentPosition);
    if (semi < 0) {
      semi = statement.length();
    }
    String substring = statement.substring(currentPosition, semi);
    currentPosition = semi + 1;
    System.out.println("Qualifier args: " + substring);
    for(StructureElement structure: structures) {
      switch(structure) {
        case EXPRESSION_LIST:
          ArrayList<Expression> expressions = splitExpressions(substring);
          this.extendedModeExpressionList.addAll(expressions);
          break;
          
        case EXPRESSION:
          ArrayList<Expression> expression = splitExpressions(substring);
          System.out.println("Adding " + expression.size() + " expressions");
          this.extendedModeExpressionList.addAll(expression);
          break;

        case IDENTIFIER:
          int index = doIdentifierInline(substring);
          try {
            substring = substring.substring(index+1);
          } catch (StringIndexOutOfBoundsException sioobe) {
            // end of args
            return;
          }
          break;

        default:
          throw new SyntaxError("Extended mode only supports expressions lists and identifiers");
      }
    }
  }
  
  private void doIdentifierList(String line) throws SyntaxError { 
    String[] ids = line.split(",");
    for(int i = 0; i < ids.length; i++) {
      String id = ids[i].trim();
      if(id.indexOf("[") > 0) {
        throw new SyntaxError("Reading directly into an array is not currently supported, use a separate assignment");
      }
      identifiers.add(id);
      System.out.println("Identifier: " + id);
    }
  }
  
  private int doIdentifierInline(String line) throws SyntaxError { 
    int index = line.indexOf('(');
    if (index < 0) {
      index = line.indexOf('[');
      if (index < 0) {
        if (index < 0) {
          index = line.indexOf(',');
          if (index < 0) {
            index = line.length();
          }
        }
      }
    }
    identifiers.add(line.substring(0, index).trim());
    System.out.println("Identifier: " + identifiers.get(0));
    return index;
  }
  
  private void doIdentifier(String line) throws SyntaxError { 
    int index = doIdentifierInline(line);
    currentPosition = currentPosition + index + 1;
  }
  
  private void doFunctionAssignment(String assign) throws SyntaxError { 
    System.out.println("Assignment: " + assign);
    int equals = assign.indexOf('=');
    if (equals < 0 || (assign.length() < (equals+1))) {
      throw new SyntaxError("Identifer must define (var_list) = [expression]");
    }

    String assignmentVariablesString = assign.substring(0, equals).trim();
    System.out.println("Vars string: " + assignmentVariablesString);
    assignmentVariablesString = getBracketedExpression(assignmentVariablesString, "(", ")");
    
    String[] split = assignmentVariablesString.split(",");
    for (String s : split) {
      s = s.trim();
      if(!(s.equals(""))) {
        assignmentVariableList.add(s);
        System.out.println("Added assignment variable: " + s.trim());
      }
    }
    String expressionListString = assign.substring(equals+1).trim();
    assignmentExpression = new Expression(expressionListString);
  }
  
  private void doAssignment(String assign) throws SyntaxError {  
    int equals = assign.indexOf('=');
    if (equals <= 0 || (assign.length() < (equals+1))) {
      throw new SyntaxError("Assignment must define a variable, var = [expression]");
    }
    String assignmentString = assign.substring(0, equals).trim();
    int braStart = assignmentString.indexOf('[');
    
    if (braStart > 0) {
      String indexes = getBracketedExpression(assignmentString.substring(braStart), "[", "]");
      doExpressionList(indexes);
      assignmentString = assignmentString.substring(0, braStart);
    } 
    
    assignmentVariableList.add(assignmentString.trim());
    String expressionString = assign.substring(equals+1).trim();
    assignmentExpression = new Expression(expressionString);
    System.out.println("Assignment expression: " + expressionString);
  }
  
  private String getBracketedExpression(String assignment, String open, String close) throws SyntaxError {
    assignment = assignment.trim();
    if (!(assignment.startsWith(open) && assignment.endsWith(close))) {
      throw new SyntaxError("Bracketed expression expected");
    }
    return assignment.substring(1, assignment.length()-1);
  }
  
  private void doExpression(String expression) throws SyntaxError {
    System.out.println("Single expression: " + expression);
    expressionList.add(new Expression(expression));
  }
  
  private void doExpressionList(String expressionListString) throws SyntaxError {
    ArrayList<Expression> expressions = splitExpressions(expressionListString);
    expressionList.addAll(expressions);
  }

  private ArrayList<Expression> splitExpressions(String expressionListString) throws SyntaxError {
    String[] strings = StringUtils.split(expressionListString, ',');
    ArrayList<Expression> expressions = new ArrayList<Expression>();
    for(int i = 0; i < strings.length; i++) {
      String exp = strings[i].trim();
      if (!("".equals(exp))) {
        expressions.add(new Expression(exp));
      }
    }
    return expressions;
  }
  
  private void doArrayList(String arrayListString) throws SyntaxError {
    String expressions = getBracketedExpression(arrayListString, "[", "]");
    doExpressionList(expressions);
  }
  
  private boolean isQuoted(int position) {      
    int quoteCount = 0;
    for (int i = 0; i < position; i++) {
      if (statement.charAt(i) == '"') {
        quoteCount++;
      }
    }
    if (quoteCount % 2 == 0) {
      return false;
    }
    return true;
  }
  
  private int checkForKeyword(String keyword, int point) {
    boolean found = false;
    int ret = -1;
    while(!found && currentPosition < statement.length()) {
      int nextKw = statement.indexOf(keyword, point);
      if (nextKw > point) {
        if(isQuoted(nextKw)) {
          point = nextKw+1;
          continue;
        }
      }
      ret = nextKw;
      found = true;
    }
    return ret;
  }

  public Command getCommand() {
    return command;
  }
  
  public String[] getIdentifiers() {
    return identifiers.toArray(new String[]{});
  }

  public String[] getAssignmentVariables() {
    return assignmentVariableList.toArray(new String[]{});
  }
  
  public Expression getAssignmentExpression() {
    return assignmentExpression;
  }

  public Expression[] getExpressions() {
    Expression[] result = new Expression[expressionList.size()];
    return expressionList.toArray(result);
  }
  
  public Expression[] getExtendedModeExpressions() {
    Expression[] result = new Expression[extendedModeExpressionList.size()];
    return extendedModeExpressionList.toArray(result);
  }

  public Statement getNextStatement() {
    return nextStatement;
  }

  public int getLineNumber() {
    return line;
  }

  public int getStatementInLine() {
    return statementInLine;
  }
  
  public static void main(String[] args) {
    String[] x = new String[] {
//      "LET l = mod (10, a[6, 6])",
//      "LET a[10, 0]=9",
//      "NEXT n",
//      "DEF seconds() = INT(time()/1000)",
//      "DEF area(x,y)=x*y",
//      "DIM a[10, 10]",
//      "PRINT AT 10, 10",
//      "PRINT AT 10, 10; a[1, 5]",
//      "PRINT AT ",
//      "POP n, m",
//      "FOR n = 1 TO 10",
//      "IF n > 1 THEN GOTO x",
//      "OPEN FILE o; \"c:\\xsd\\test.txt\", 1",
//      "READ FROM o, 1000; a$",
//      "RETURN",
      "IF draw == 1 THEN DRAW s_width/2+80*(sin(-(mins+30)/30*PI)), s_height/2+80*(cos(-(mins+30)/30*PI))"
    };
    try {
      for (String s : x) {
        Statement st = new Statement(s, 0, 0);
        System.out.println(st.getNextStatement());
//        Expression[] ex = st.getExpressions();
//        Expression[] ex1 = st.getExtendedModeExpressions();
//        System.out.println(ex.length);
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
