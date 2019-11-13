
package org.happysoft.basic.command;

/**
 *
 * @author Chris Francis
 */
public enum ExtendedMode {
  
  OVER(3, StructureElement.EXPRESSION_LIST),
  AT(2, StructureElement.EXPRESSION_LIST),
  TAB(0, StructureElement.NONE),
  TCP(0, StructureElement.IDENTIFIER),
  UDP(0, StructureElement.IDENTIFIER),
  HTTP(0, StructureElement.IDENTIFIER),
  FILE(0, StructureElement.IDENTIFIER),
  FROM(0, StructureElement.IDENTIFIER, StructureElement.EXPRESSION_LIST),
  KEY(0, StructureElement.IDENTIFIER);
  
  private int numArgs;
  private StructureElement[] structure;
  
  ExtendedMode(int numArgs, StructureElement... structure) {
    this.numArgs = numArgs;
    this.structure = structure;
  }
  
  public String getMode() {
    return this.name();
  }
  
  public StructureElement[] getStructure() {
    return structure;
  }
  
  public int getNumArgs() {
    return this.numArgs;
  }
}
