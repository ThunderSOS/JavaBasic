
package org.happysoft.basic.command;

import org.happysoft.basic.Program;
import org.happysoft.basic.Statement;
import org.happysoft.basic.SyntaxError;

/**
 *
 * @author Chris
 */
public abstract class AbstractCommand implements Command {
  
  ExtendedMode extendedMode = null;
    
  public ExtendedMode[] getExtendedModes() {
    return null;
  }
  
  public void setExtendedMode(ExtendedMode mode) {
    this.extendedMode = mode;
  }
  
  public ConnectorElement[] getExpectedConnectives() {
    return new ConnectorElement[0];
  }
 
  public void execute(Program context, Statement statement) throws SyntaxError {
  }
  
  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
  
  @Override
  public boolean equals(Object another) {
    if(another instanceof Command) {
      Command an = (Command) another;
      return an.getClass() == getClass();
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = ("" + getClass()).hashCode();
    return hash;
  }
}
