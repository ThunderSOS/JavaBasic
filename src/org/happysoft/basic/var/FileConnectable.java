/**
 * Copyright 2013. All rights reserved. 
 */
package org.happysoft.basic.var;

import java.io.*;
import org.happysoft.basic.SyntaxError;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class FileConnectable implements Connectable {
  
  private File underlying = null;
  protected BufferedOutputStream bout = null;
  protected BufferedInputStream bin = null;
  private boolean writeFlag = false;
  private boolean append = false;
  
  public FileConnectable(String filename, boolean append) throws SyntaxError {     
    this.underlying = new File(filename);
    this.append = append;
    try {
      if(!underlying.exists()) {
        System.out.println("Create new");
        underlying.createNewFile();
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
      throw new SyntaxError("Could not open file");
    }
  }

  public void close() throws SyntaxError {
    try {
      if(writeFlag) {
        bout.flush();
      }
      bout.close();
    } catch (IOException ioe) {
      throw new SyntaxError("Write failed");
    }
  }

  public void connect(String... params) throws SyntaxError { 
    try {
      writeFlag = false;
      bout = new BufferedOutputStream(new FileOutputStream(underlying, append));
      bin = new BufferedInputStream(new FileInputStream(underlying));
    } catch (FileNotFoundException fnfe) {
      throw new SyntaxError("File not found: ");
    }
  }
  
  public byte[] read(int from, int to) throws SyntaxError {
    byte[] b = new byte[to-from];
    try {
      bin.read(b, from, to-from);
    } catch (IOException ex) {
      throw new SyntaxError("Read error");
    }
    return b;
  }

  public void write(byte[] bytes) throws SyntaxError {
    try {
      bout.write(bytes);
      writeFlag = true;
    } catch (IOException ioe) {
      throw new SyntaxError("Write failed");
    }
  }
  
  public long len() {
    return underlying.length();
  }
  
  public ConnectableType getConnectableType() {
    return ConnectableType.FILE;
  }

}
