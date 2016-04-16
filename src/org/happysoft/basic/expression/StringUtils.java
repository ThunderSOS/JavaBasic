/**
 * Copyright 2013. All rights reserved. 
 */
package org.happysoft.basic.expression;

import java.util.ArrayList;
import org.happysoft.basic.SyntaxError;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class StringUtils {
  
  public static String[] split(String token, char splitChar) throws SyntaxError {
    // this sequence is the same as in Statement, refactor. 
    ArrayList<String> strings = new ArrayList<String>();
    int braCount = 0;
    int quoteCount = 0;
    
    char[] chars = token.toCharArray();
    
    StringBuilder sb = new StringBuilder();
    
    for (int i = 0; i < chars.length; i++) { 
      char c = chars[i];
      if (c == '\'' || c == '"') {
        quoteCount++;
      }
      if (c == '(' || c == '[') {
        braCount++;
      } 
      if(c == ')' || c == ']') {
        braCount--;
      } 
      if(c == splitChar && braCount == 0 && quoteCount%2 == 0) {
        strings.add(sb.toString());
        sb = new StringBuilder();
        continue;
      }
      sb.append(c);
      if(i == chars.length-1 && c != splitChar) {
        strings.add(sb.toString());
        break;
      }
    }

    String[] ret = (String[])strings.toArray(new String[strings.size()]);
    return ret;
  }
}
