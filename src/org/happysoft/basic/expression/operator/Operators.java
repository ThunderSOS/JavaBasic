package org.happysoft.basic.expression.operator;

import java.util.HashMap;

/**
 *
 * @author chris
 */
public enum Operators {
  BOOLEAN_NOT_EQUALS("!=", new BooleanNotEqual()),
  BOOLEAN_EQUALS("==", new BooleanEquals()),
  BOOLEAN_AND("&&", new BooleanAnd()),
  BOOLEAN_OR("||", new BooleanOr()),
  LESS_THAN("<", new LessThan()),
  GREATER_THAN(">", new GreaterThan()),
  LESS_THAN_EQUALS("<=", new LessThanEquals()),
  GREATER_THAN_EQUALS(">=", new GreaterThanEquals()),
  AND("&", new And()),
  OR("|", new Or()),
  XOR("^", new Xor()),
  ADD("+", new Add()),
  MINUS("-", new Minus()),
  MULTIPLY("*", new Multiply()),
  DIVIDE("/", new Divide());

  private static final HashMap<String, Operator> map = new HashMap<String, Operator>();

  static {
    for (Operators o : Operators.values()) {
      map.put(o.symbol, o.operator);
    }
  }

  private String symbol;
  private Operator operator;

  private Operators(String symbol, Operator operator) {
    this.symbol = symbol;
    this.operator = operator;
  }

  public static Operator getBySymbol(String symbol) {
    return map.get(symbol);
  }

}
