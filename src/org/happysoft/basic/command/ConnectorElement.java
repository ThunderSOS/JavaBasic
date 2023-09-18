
package org.happysoft.basic.command;

/**
 *
 * @author Chris
 */
public enum ConnectorElement {
  //EQUALS("=", false),
  THEN("THEN", false),
  //ELSE("ELSE", true),
  TO("TO", false), 
  STEP("STEP", true),
  ELSE("ELSE", true)
  //INTO("INTO", true),
  ;
  
  private boolean optional = false;
  private String keyword;
  
  private ConnectorElement(String keyword, boolean optional) {
    this.keyword = keyword;
    this.optional = optional;
  }
  
  public boolean isOptional() {
    return optional;
  }
  
  public String getKeyword() {
    return keyword;
  }
}
