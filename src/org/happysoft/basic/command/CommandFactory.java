
package org.happysoft.basic.command;

import org.happysoft.basic.SyntaxError;

/**
 * @author Chris
 */
public class CommandFactory {

	private static CommandFactory instance = new CommandFactory();
	
	private CommandFactory() {
	}

	public static CommandFactory getInstance() {
		return instance;
	}

	public Command getCommand(String command) throws SyntaxError {
		Command c = Commands.getBySymbol(command.toUpperCase());
		if(c == null) {
			throw new SyntaxError("Unknown command: " + command);
		}
		return c;
	}
	
}
