
package org.happysoft.basic.var;

import java.util.HashMap;
import org.happysoft.basic.SyntaxError;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class StringTable {

	private static HashMap<String, String> vars = new HashMap<String, String>();

	private static StringTable instance;

  static {
    instance = new StringTable();
  }

	private StringTable() {
	}

	public static StringTable getInstance() {
		return instance;
	}

	public String getVariable(String name) throws SyntaxError {
		String s = vars.get(name);
		if(s == null) {
			throw new SyntaxError("String " + name + " is not defined");
		}
		return s;
	}

	public void setStringVariable(String name, String value) {
		vars.put(name, value);
    System.out.println("Assigned " + value + " to " + name);
	}
}
