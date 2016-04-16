/**
 * Copyright 2013. All rights reserved. 
 */
package org.happysoft.basic.var;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import org.happysoft.basic.SyntaxError;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class HttpConnectable implements Connectable {
  
  private HttpURLConnection s = null;
  protected BufferedOutputStream bout = null;
  protected BufferedInputStream bin = null;

  public HttpConnectable() {
  }
  
  public void connect(String... params) throws SyntaxError {
    String host = params[0];
    try {
      s = (HttpURLConnection)new URL(host).openConnection();
      s.setDoInput(true);
      s.setDoOutput(true);
      s.connect();
      bin = new BufferedInputStream(s.getInputStream());
      System.out.println("Http connection established");
      
    } catch(UnknownHostException uhe) {
      throw new SyntaxError("Unknown host: " + uhe.getMessage());
      
    } catch (IOException ioe) {
      ioe.printStackTrace();
      throw new SyntaxError("I/O exception");
    }
  }

  public byte[] read(int max) throws SyntaxError {
    byte[] b = new byte[max];
    try {
      bin.read(b);
    } catch (IOException ex) {
      throw new SyntaxError("Read error");
    }
    return b;
  }

  public void write(byte[] bytes) throws SyntaxError {
    try {
      bout.write(bytes);
    } catch (IOException ex) {
      throw new SyntaxError("Write error");
    }
  }

  public void close() throws SyntaxError {
    s.disconnect();
  }

  public ConnectableType getConnectableType() {
    return ConnectableType.HTTP;
  }
  
  public static void main(String[] args) throws SyntaxError {
    HttpConnectable h = new HttpConnectable();
    h.connect("http://www.google.co.uk");
    byte[] bytes = h.read(1000);
    String s = new String(bytes);
    System.out.println(s);
    h.close();
  }

}
