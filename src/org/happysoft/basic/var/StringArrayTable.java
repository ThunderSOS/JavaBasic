
package org.happysoft.basic.var;

import java.util.HashMap;
import org.happysoft.basic.SyntaxError;

/**
 * @author Chris
 */
public class StringArrayTable {

	private static HashMap<String, Array> vars = new HashMap<String, Array>();

	private static StringArrayTable instance;

	static {
		instance = new StringArrayTable();
	}

	private StringArrayTable() {
	}

	public static StringArrayTable getInstance() {
		return instance;
	}

  public void createArray(String name, int... dimension) throws SyntaxError {
		Array a = new Array(dimension);
    vars.put(name, a);
	}
  
  public void setValue(String name, String value, int... dimension) throws SyntaxError {
    Array a = vars.get(name);
    if (a == null) {
      throw new SyntaxError("Array " + name + " not defined");
    }
    a.set(value, dimension);
    System.out.println("Assigned " + value + " to " + name + "[" + dimension[0] + ", ... ]");
  }
  
  public String getValue(String arrayName, int... dimension) throws SyntaxError {
    Array a = vars.get(arrayName);
    return a.getString(dimension);
  }
  
  public static void main(String[] args) {
    try {
      StringArrayTable a = StringArrayTable.getInstance();
      a.createArray("a", 3, 3);
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          int x = i*3+j;
          a.setValue("a", "" + x, i, j);
        }
      }
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          System.out.print(a.getValue("a", i, j) + ",");
        }
      }
    } catch (SyntaxError ex) {
      ex.printStackTrace();
    }
  }
  
}
