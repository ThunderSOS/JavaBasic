
package org.happysoft.basic.command;

import java.util.Stack;

/**
 * @author Chris
 */
public class GoSubStack {
   
  private Stack<int[]> returnStack = new Stack<int[]>();
  
  private static GoSubStack instance = new GoSubStack();
  
  private GoSubStack() {
  }
  
  public static GoSubStack getInstance() {
    return instance;
  }
  
  public void push(int line, int statement) {
    int[] x = new int[] {line, statement};
    returnStack.push(x);            
  }
  
  public int[] getReturnJump() {
    return returnStack.pop();
  }

}
