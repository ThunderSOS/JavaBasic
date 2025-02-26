/**
 * Copyright 2013. All rights reserved. 
 */
package org.happysoft.basic.command;

import java.util.ArrayList;
import java.util.List;
import org.happysoft.basic.expression.ExpressionResult;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class DataCache {
  
  private int lastRead = 0;
  
  private final List<ExpressionResult> values = new ArrayList<ExpressionResult>();
  
  private static final DataCache instance = new DataCache();
  
  private DataCache() {
  }
  
  public static DataCache getInstance() {
    return instance;
  }
  
  public void add(ExpressionResult newValue) {
    values.add(newValue);
  }
  
  public ExpressionResult getNext() {
    ExpressionResult ret = values.get(lastRead);
    lastRead++;
    return ret;
  }
  
  public void reset() {
    values.clear();
    lastRead = 0;
  }

}
