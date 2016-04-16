
package org.happysoft.basic.command;

import org.happysoft.basic.Program;
import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.Statement;

/**
 * @author Chris Francis
 */
public interface Command {  
    public ExtendedMode[] getExtendedModes();
    public void setExtendedMode(ExtendedMode extendedMode); 
    public StructureElement[] getCommandStructure();
    public ConnectorElement[] getExpectedConnectives();
    public void execute(Program context, Statement statement) throws SyntaxError;
}
