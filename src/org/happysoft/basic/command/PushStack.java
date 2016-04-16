
package org.happysoft.basic.command;

import java.util.Stack;
import org.happysoft.basic.expression.Argument;

/**
 *
 * @author Chris
 */
public class PushStack {

  private Stack<Argument> stack = new Stack<Argument>();
  private static PushStack instance = new PushStack();
  
  private PushStack() {
  }
  
  public static PushStack getInstance() {
    return instance;
  }
  
  public void push(Argument a) {
    stack.push(a);         
    System.out.println(a.getStringValue() + " added to stack");
  }
  
  public Argument pop() {
    return stack.pop();
  }
}
