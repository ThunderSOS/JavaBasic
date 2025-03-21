/**
 * Copyright 2013. All rights reserved. 
 */
package org.happysoft.basic.var;

import java.util.HashMap;
import org.happysoft.basic.SyntaxError;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class ConnectableTable {

	private static HashMap<String, Connectable> connections = new HashMap<String, Connectable>();

	private static ConnectableTable instance;

	static {
		instance = new ConnectableTable();
	}

	private ConnectableTable() {
	}

	public static ConnectableTable getInstance() {
		return instance;
	}
  
  public void add(String identifier, Connectable c) {
    connections.put(identifier, c);
  }
  
  public void connect(String identifier, String... params) throws SyntaxError {
    Connectable c = connections.get(identifier);
    c.connect(params);
  }
  
  public byte[] read(String identifier, int from, int to) throws SyntaxError {
    Connectable c = connections.get(identifier);
    return c.read(from, to);
  }
  
  public void write(String identifier, byte[] bytes) throws SyntaxError {
    Connectable c = connections.get(identifier);
    c.write(bytes);
  }
  
  public long len(String identifier) throws SyntaxError {
    Connectable c = connections.get(identifier);
    return c.len();
  }
  
  public void close(String identifier) throws SyntaxError {
    Connectable c = connections.get(identifier);
    c.close();
  }
  
}
