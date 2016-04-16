
package org.happysoft.basic.command;

import java.util.HashMap;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.var.VariableTable;

/**
 * @author Chris
 */
public class ForVariableCache {
  
  private HashMap<String, Long> stepMap = new HashMap<String, Long>();
  private HashMap<String, int[]> gotoMap = new HashMap<String, int[]>();
  private HashMap<String, Long> maxMap = new HashMap<String, Long>();
  
  private static ForVariableCache instance = new ForVariableCache();
  
  private ForVariableCache() {
  }
  
  public void initializeForLoop(int gotoLine, int gotoStatement, String variableName, long start, long max, Long step) {
    if (step == null) {
      step = 1L;
    }
    int[] gotoJump = new int[]{ gotoLine, gotoStatement };
    VariableTable.getInstance().setVariable(variableName, start);
    gotoMap.put(variableName, gotoJump);
    stepMap.put(variableName, step);
    maxMap.put(variableName, max);    
  }
  
  public static ForVariableCache getInstance() {
    return instance;
  }
  
  public int[] getGotoJump(String variableName) {
    return gotoMap.get(variableName);
  }
  
  public boolean next(String variableName) throws SyntaxError {
    long step = stepMap.get(variableName);
    long max = maxMap.get(variableName);
    int currentValue = (int) VariableTable.getInstance().getVariable(variableName);
    currentValue += step;
    VariableTable.getInstance().setVariable(variableName, currentValue);
    if (currentValue > max) {  // if step negative
      return false;
    }
    return true;
  }
  
}
