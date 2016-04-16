
package org.happysoft.basic.command;

/**
 * @author Chris
 * The structure of a command is built by StructureElements and connectives. 
 * e.g.
 * IF <expression> THEN <command>
 * LET <identifier> = <expression>
 */
public enum StructureElement {
  ASSIGNMENT,
  EXPRESSION,
  EXPRESSION_LIST, 
  COMMAND,
  IDENTIFIER,
  IDENTIFIER_LIST,
  ARRAY_LIST,
  FUNCTION_ASSIGNMENT,
  NONE
}
