
package org.happysoft.basic.command;

import java.util.HashMap;

/**
 * All available commands mapped to their implementation classes. 
 * @author Chris Francis
 */
public enum Commands {
  
  // Define
  LET("LET", new LET()),  
  DEF("DEF", new DEF()),
  DIM("DIM", new DIM()),
  
  // Branching
  IF("IF", new IF()),
  GOTO("GOTO", new GOTO()),
  GOSUB("GOSUB", new GOSUB()),
  RETURN("RETURN", new RETURN()),
  
  // Looping
  FOR("FOR", new FOR()),
  NEXT("NEXT", new NEXT()),
  
  // File access
  OPEN("OPEN", new OPEN()),
  CLOSE("CLOSE", new CLOSE()),
  
  // Halt
  PAUSE("PAUSE", new PAUSE()),
  STOP("STOP", new STOP()),
  
  // PUSH and POP
  PUSH("PUSH", new PUSH()),
  POP("POP", new POP()),
  
  // READ, DATA and RESTORE
  READ("READ", new READ()),
  DATA("DATA", new DATA()),
  RESTORE("RESTORE", new RESTORE()),
  
  // Windowing commands
  WINDOW("WINDOW", new WINDOW()),
  DRAW("DRAW", new DRAW()),
  ELLIPSE("ELLIPSE", new ELLIPSE()),
  PLOT("PLOT", new PLOT()),
  INK("INK", new INK()),
  PAPER("PAPER", new PAPER()),
  CLS("CLS", new CLS()),
  PRINT("PRINT", new PRINT()),
  
  // Test assertions
  ASSERT("ASSERT", new ASSERT()),
  ;
  
	private static final HashMap<String, Command> map = new HashMap<String, Command>();

	static {
		for(Commands c : Commands.values()) {
			map.put(c.string, c.command);
		}
	}

	private String string;
	private Command command;

	private Commands(String string, Command command) {
		this.string = string;
		this.command = command;
	}

	public static Command getBySymbol(String string) {
		return map.get(string);
	}
}
