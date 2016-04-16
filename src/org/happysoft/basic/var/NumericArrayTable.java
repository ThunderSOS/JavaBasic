
package org.happysoft.basic.var;

import java.util.HashMap;
import org.happysoft.basic.SyntaxError;

/**
 * @author Chris
 */
public class NumericArrayTable {

	private static HashMap<String, Array> vars = new HashMap<String, Array>();

	private static NumericArrayTable instance;

	static {
		instance = new NumericArrayTable();
	}

	private NumericArrayTable() {
	}

	public static NumericArrayTable getInstance() {
		return instance;
	}

  public void createArray(String name, int... dimension) throws SyntaxError {
		Array a = new Array(dimension);
    vars.put(name, a);
	}
  
  public void setValue(String name, double value, int... dimension) throws SyntaxError {
    Array a = vars.get(name);
    a.set(value, dimension);
    System.out.println("Assigned " + value + " to " + name + "[" + dimension[0] + ", ... ]");
  }
  
  public double getValue(String arrayName, int... dimension) throws SyntaxError {
    Array a = vars.get(arrayName);
    if (a == null) {
      throw new SyntaxError("Array " + arrayName + " not defined");
    }
    return a.getNumeric(dimension);
  }
  
  
  public static void main(String[] args) {
    try {
      NumericArrayTable a = NumericArrayTable.getInstance();
      a.createArray("a", 3, 3);
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          int x = i*3+j;
          a.setValue("a", x, i, j);
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
