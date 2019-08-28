
package org.happysoft.basic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.happysoft.basic.command.Command;
import org.happysoft.basic.command.AbstractCommand;
import org.happysoft.basic.expression.LocalArgumentMap;
import org.happysoft.basic.expression.StringUtils;

/**
 * @author Chris
 */
public class Program {
  
  private DisplayWindow display = new DisplayWindow(this);
  private Integer currentLineNumber = 0;
  private Integer currentStatementNumber = 0;
  private Integer dataLineNumber = 0;
  private Integer dataStatementNumber = 0;
  private boolean stop = false;
  
  public boolean readDataFlag = false;
  public static int FOUND = 1;
  public static int NOT_FOUND = 0;
 
  private Statement[] currentLine;

  Set<Integer> lineNumbers = new TreeSet<Integer>();
  TreeMap<Integer, String> program = new TreeMap<Integer, String>();
 
  
  public Program() {
  }
  
  public String loadProgram(String filename) throws FileNotFoundException, IOException {
    BufferedReader br = new BufferedReader(new FileReader(filename));
    String programIn = "";
    while(true) {
      String s = br.readLine();
      if (s == null) {
        break;
      }
      if(s.startsWith("#")) {
        continue;
      }
      if(s.trim().equals("")) {
        continue;
      }
      programIn = programIn + s + "\n";
    }
    return programIn;
  }

  public DisplayWindow getDisplayWindow() {
    return display;
  }
  
  private void parseProgram(String program) throws SyntaxError {
    String[] lines = program.split("\n");
    for (String line : lines) {
      readLine(line);
    }
  }
  
  private void readLine(String line) throws SyntaxError {
    System.out.println("Loading line: " + line);
    line = line.trim();
    int lineNumberEnd = line.indexOf(' ');
    if (lineNumberEnd < 0) {
      throw new SyntaxError("Line number expected");
    }
    try {
      int lineNumber = Integer.parseInt(line.substring(0, lineNumberEnd));
      String codeLine = line.substring(lineNumberEnd+1, line.length());
      lineNumbers.add(lineNumber);
      String existingLine = program.get(lineNumber);
      if(existingLine == null) {
        program.put(lineNumber, codeLine);
      } else {
        throw new SyntaxError("Line number " + line + " already added");
      }
    
    } catch (NumberFormatException nfe) {
      throw new SyntaxError("Line number must be a number!");
    }
  }
  
  public void stop() {
    stop = true;
  }
  
  public void scanFor(Class<? extends AbstractCommand> toFind, String identifier, int line, int statement, boolean execute) throws SyntaxError {
    readDataFlag = execute;
    setLineAndStatement(line, statement);
    
    boolean found = false;
    while(!found) {
      Statement s = getNextStatement(true);      
      if (s == null) {
        break;
      }
      Command command = s.getCommand();
      if(command.getClass().equals(toFind)) {
        if(identifier != null) {
          if(s.getIdentifiers()[0].equals(identifier)) {
            found = true;
          } 
        } else {
          found = true;
        }
      }
      if(found && execute) {
        command.execute(this, s);
      }
      continue;
    }   
    readDataFlag = false;
  }
  
  public void run(int fromLine) throws SyntaxError { 
    currentLineNumber = fromLine;
    currentStatementNumber = 0;
    try {
      while(!stop) {
        Statement s = getNextStatement(false);
        if (s == null) {
          break;
        }
        Command command = s.getCommand();
        System.out.println("Executing statement: " + currentLineNumber + ":" + currentStatementNumber + "; " + command);
        command.execute(this, s);
      }
    } catch (SyntaxError se) {
      System.out.println("Syntax error at " + currentLineNumber + ":" + currentStatementNumber + " - " + se.getMessage());
    }
    display.finish();
    System.out.println("Exit");
  }
  
  private Statement getNextStatement(boolean commandOnly) throws SyntaxError {
    if (currentLine != null && currentStatementNumber < currentLine.length) {
      Statement next = currentLine[currentStatementNumber];
      currentStatementNumber++;
      return next;
    } else {      
      currentLineNumber++;
      currentStatementNumber = 0;
      loadNextLine();
      if (currentLine != null) {
        return getNextStatement(commandOnly);
      }
    }
    return null;
  }
  
  protected void loadNextLine() throws SyntaxError {
    currentLineNumber = program.ceilingKey(currentLineNumber); 
    if (currentLineNumber == null) {
      currentLine = null;
      return;
    }
    String line = program.get(currentLineNumber);
    String[] rawStatements = StringUtils.split(line, ':');
    currentLine = new Statement[rawStatements.length];
    for(int i = 0; i < rawStatements.length; i++) {
      // if a NEXT or RETURN has jumped to the middle of a line we
      // do not want to re-execute any assignments that may have 
      // happened earlier so skip them. 
      if (i < currentStatementNumber) {
        continue;
      }
      String rawStatement = rawStatements[i].trim();
      currentLine[i] = new Statement(rawStatement, currentLineNumber, i);
    }
  }
  
  
  public void setLineAndStatement(int line, int statement) throws SyntaxError {
    System.out.println("Jumping: " + line + "," + statement);
    currentStatementNumber = statement;
    currentLineNumber = line;
    loadNextLine();
  }
  
  public int getCurrentLine() {
    return currentLineNumber;
  }
  
  public int getCurrentStatement() {
    return currentStatementNumber;
  }
   
  public int getDataLineNumber() {
    return dataLineNumber;
  }
  
  public int getDataStatementNumber() {
    return dataStatementNumber;
  }
  
  public void setCurrentLineNumber(Integer currentLineNumber) {
    this.currentLineNumber = currentLineNumber;
  }

  public void setCurrentStatementNumber(Integer currentStatementNumber) {
    this.currentStatementNumber = currentStatementNumber;
  }

  public void setDataLineNumber(Integer dataLineNumber) {
    this.dataLineNumber = dataLineNumber;
  }

  public void setDataStatementNumber(Integer dataStatementNumber) {
    this.dataStatementNumber = dataStatementNumber;
  }
  
  public static void main(String[] args) {
    try {
      Program p = new Program();
      
      String program = p.loadProgram("C:\\NetBeansProjects\\Expression\\test\\read_1.bas");


      p.parseProgram(program);
      p.run(0);
      
    } catch (SyntaxError se) {
      se.printStackTrace();
      
    } catch (Exception ioe) {
      ioe.printStackTrace();
    }
  }
  
    
  
  //String programIn = "10 FOR n = 1+ LEN (\"TO\") TO 20    STEP 6";

  //String programIn = "10 GOTO 20 : PRINT 12345 \n20 PRINT \"Done\"";
  //String programIn = "10 GOTO 100\n20 PRINT \"hello\"\n30 RETURN\n100 GOSUB 20\n110 PRINT \"finished\"\n120 PRINT 3";
  
  //String programIn = "10 DIM a[10, 20] : LET a[5, 0] = 15 : LET a[5, 15] = 17 : PRINT a[5, 0] : PRINT -a[5, a[5, 0]]";
  //String programIn = "10 FOR n=0 TO : PRINT n : \n20 NEXT n";  // wrong
  
  
//  String program = "1 WINDOW \"Hello\", 640, 480 : PAUSE 1000 \n10 DIM a[10] : LET a[0] = 60: DEF seconds(x, y)=INT(MOD(x/1000, a[y]))\n" + 
//                     "40 LET x = time()\n" + 
//                     "50 PRINT seconds(x, 0)";

  
  //String programIn = "10 WINDOW \"Hello\", 640, 480 : PAUSE 5000";
}
