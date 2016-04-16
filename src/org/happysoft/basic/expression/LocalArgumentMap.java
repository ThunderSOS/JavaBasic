
package org.happysoft.basic.expression;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Chris
 */
public class LocalArgumentMap {
  private Map<String, Argument> map = new HashMap<String, Argument>();
  
  public boolean hasKey(String varName) {
    return map.containsKey(varName);
  }
  
  public Argument getKey(String varName) {
    System.out.println("Getting arg " + varName + " from local args [" + map.get(varName) + "]");
    return map.get(varName);
  }
  
  public void addKey(String varName, Argument arg) {
    System.out.println("***** set arg: " + varName + " = " + arg.value);
    map.put(varName, arg);
  }
}
