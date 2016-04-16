/**
 * Copyright 2013. All rights reserved. 
 */
package org.happysoft.basic.var;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.happysoft.basic.SyntaxError;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class TcpConnectable implements Connectable {
  
  private Socket s = null;
  protected BufferedOutputStream bout = null;
  protected BufferedInputStream bin = null;

  public TcpConnectable() {
  }
  
  public void connect(String... params) throws SyntaxError {
    String host = params[0];
    int port = Integer.parseInt(params[1]);
    try {
      s = new Socket(host, port);
      bout = new BufferedOutputStream(s.getOutputStream());
      bin = new BufferedInputStream(s.getInputStream());
      System.out.println("Socket established");
      
    } catch(UnknownHostException uhe) {
      throw new SyntaxError("Unknown host: " + uhe.getMessage());
      
    } catch (IOException ioe) {
      throw new SyntaxError("I/O exception");
    }
  }

  public byte[] read(int max) throws SyntaxError {
    byte[] b = new byte[max];
    try {
      bin.read(b);
    } catch (IOException ex) {
      Logger.getLogger(TcpConnectable.class.getName()).log(Level.SEVERE, null, ex);
    }
    return b;
  }

  public void write(byte[] bytes) throws SyntaxError {
    try {
      bout.write(bytes);
    } catch (IOException ex) {
      Logger.getLogger(TcpConnectable.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void close() throws SyntaxError {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public ConnectableType getConnectableType() {
    return ConnectableType.TCP;
  }

}
