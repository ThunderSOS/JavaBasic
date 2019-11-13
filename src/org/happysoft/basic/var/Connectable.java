/**
 * Copyright 2013. All rights reserved. 
 */
package org.happysoft.basic.var;

import org.happysoft.basic.SyntaxError;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public interface Connectable {
  public void connect(String... params) throws SyntaxError;
  public byte[] read(int from, int to) throws SyntaxError;
  public void write(byte[] bytes) throws SyntaxError;
  public void close() throws SyntaxError;
}
