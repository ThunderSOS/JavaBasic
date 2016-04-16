
package org.happysoft.basic.var;

import java.util.HashMap;
import org.happysoft.basic.SyntaxError;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class VariableTable {

	enum access {
		READ_ONLY,
		READ_WRITE
	}

	private static HashMap<String, Double> vars = new HashMap<String, Double>();

	private static VariableTable instance;

	static {
		instance = new VariableTable();
	}

	private VariableTable() {
    vars.put("PI", Math.PI);
		vars.put("E", Math.E);
    vars.put("True", 1d);
    vars.put("False", 0d);
	}

	public static VariableTable getInstance() {
		return instance;
	}

	public double getVariable(String name) throws SyntaxError {
		Double d = vars.get(name);
		if(d == null) {
			throw new SyntaxError("Variable " + name + " is not defined");
		}
		return d;
	}

  public void setVariable(String name, String value) {
		setVariable(name, Double.valueOf(value));
	}
    
	public void setVariable(String name, double value) {
		vars.put(name, value);
    System.out.println("Assigned " + value + " to " + name);
	}
}
