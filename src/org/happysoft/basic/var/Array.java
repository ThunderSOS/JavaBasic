
package org.happysoft.basic.var;

import org.happysoft.basic.SyntaxError;

/**
 * @author Chris
 */
public class Array {
  
  private int[] sizes;
  private Object[] storage;
  private int base;
  
  public Array(int... dimSizes) throws SyntaxError {
    this.sizes = dimSizes;
    int storeSize = getStoreSize(dimSizes);
    storage = new Object[storeSize];
  }
  
  private int getMaxDimension(int[] dimensions) {
    int x = 0;
    for (int i : dimensions) {
      if (i > x) {
        x = i;
      }
    }
    return x;
  }
  
  private int getStoreSize(int[] dimSizes) throws SyntaxError {
    base = getMaxDimension(dimSizes);
    long storeSize = 1;
    for (int x : dimSizes) {
      storeSize *= base;
    }
    if (storeSize > 65535) {
      throw new SyntaxError("Array index too large");
    }
    return (int)storeSize;
  }  
  
  public void setAll(byte[] values, int... index) throws SyntaxError {
    int position = getStoragePosition(index);
    for(int i = 0; i < values.length; i++) {
      storage[position+i] = (double)values[i];
    }
  }
  
  public void set(double value, int... dimension) throws SyntaxError {
    int position = getStoragePosition(dimension);
    storage[position] = value;
  }
  
  public void set(String value, int... dimension) throws SyntaxError {
    int position = getStoragePosition(dimension);
    storage[position] = value;
  }
  
  public double getNumeric(int... dimension) throws SyntaxError{
    try {
      int position = getStoragePosition(dimension);
      return (Double)storage[position];
      
    } catch (NullPointerException npe) {
      return 0;
    }
  }
  
  public String getString(int... dimension) throws SyntaxError {
    try {
      int position = getStoragePosition(dimension);
      return (String)storage[position];
      
    } catch (NullPointerException npe) {
      return "";
    }
  }
  
  private int getStoragePosition(int... dimension)throws SyntaxError {
    int position = 0;
    int mybase = 1;
    for (int i = 0; i < dimension.length; i++) {
      if(sizes[i] < dimension[i]) { 
        throw new SyntaxError("Array index out of bounds");
      }
      position = position + (mybase*dimension[i]);
      mybase *= base;
    }
    return position;
  }
 
}
