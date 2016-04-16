
package org.happysoft.basic.expression;

import org.happysoft.basic.SyntaxError;

public class ExpressionTokenizer {

  private int currentPosition = 0;
	
  private String expression;

  private TokenType currentType = null;
  private TokenType nextType = null;
  private TokenType previousType = null;
	
	private static String OPERATORS="+-*/!&|^=<>";
	//private static String UNARY_OPERATORS="+-!";
  
  public ExpressionTokenizer(String expression) throws SyntaxError {
    this.expression = expression.trim();
    System.out.println("Expression = " + expression);
    if (expression.trim().equals("")) {
      throw new SyntaxError("Empty expression");
    }
  }

  public boolean hasMoreTokens() throws SyntaxError {
		skipWhitespace();
    return currentPosition < expression.length();
  }       

  public String getNextToken() throws SyntaxError {  
    System.out.println("Get next token: " + currentType);
    previousType = currentType;
    skipWhitespace();
    char c = expression.charAt(currentPosition);
    if(c == '"' || c == '\'') {
      currentType = TokenType.STRING;
    } else {
			if(c == '(') {
				currentType = TokenType.EXPRESSION;
			} else {
				if(Character.isDigit(c)) {
					currentType = TokenType.NUMBER;
				} else {
					if(Character.isLetter(c)) {
						currentType = TokenType.VARIABLE;
            // possible change to support AND, OR, XOR, etc
//            if(previousType == TokenType.NUMBER || previousType == TokenType.VARIABLE) {
//              System.out.println("*** change current type to OPERATOR??");
//            }
					} else {
            if (c == '[' || c == ']') {
              currentType = TokenType.INDEX_EXPRESSION;
              
            } else {
              if(OPERATORS.indexOf(c)>=0) {
                currentType = TokenType.OPERATOR;
                System.out.println("Check unary: PreviousType = " + previousType + ", nextType = " + nextType);
                if(previousType == null || previousType == TokenType.OPERATOR || previousType == TokenType.FUNCTION) {
                  currentType = TokenType.UNARY_OPERATOR;
                  System.out.println("Current type changed to unary");
                }
              } else {
                throw new SyntaxError("Unrecognized character in expression: " + c);
              }
            }
					}
				}
			}
		}
    String token = getToken(currentType);
    return token;
  }

	private char getNextChar() {
		int x = currentPosition+1;
		char c = expression.charAt(x);
		while(Character.isWhitespace(c) && x < expression.length()) {
			x++;
			c = expression.charAt(x);
		}
		return c;
	}

  private void lookAhead() throws SyntaxError {
    //System.out.println("Called lookAhead");
    int tempPos = currentPosition;
    skipWhitespace();
    char c = expression.charAt(currentPosition);
    
		if(c == '(') {
			nextType = TokenType.EXPRESSION;      
		} else {
      if(c == '[') {
        nextType = TokenType.INDEX_EXPRESSION;
        currentType = TokenType.ARRAY;
      } else {
        if(Character.isDigit(c)) {
          nextType = TokenType.NUMBER;
        } else {
          if(Character.isLetter(c)) {
            nextType = TokenType.VARIABLE;
          } else {
            nextType = TokenType.OPERATOR;
            if(c == '-') {
              c = getNextChar();
              if(Character.isDigit(c)) {
                nextType = TokenType.NUMBER;
              } else {
                if(Character.isLetter(c)) {
                  nextType = TokenType.VARIABLE;
                } else {
                  throw new SyntaxError("variable or number expected");
                }
              }
            } else {
              if (OPERATORS.indexOf(c) < 0) {
                throw new SyntaxError("Unexpected token: " + c);
              }
            }
          }
        }
      }
    }
    currentPosition = tempPos;
  }
  
  public TokenType getTokenType() {
    return currentType;
  }

  private String getToken(TokenType type) throws SyntaxError {
		if(type == TokenType.NUMBER) {
      return getNumber();
    }
    if(type == TokenType.VARIABLE) {
      return getVariableOrFunction();
    }
		if(type == TokenType.EXPRESSION) {
			return getExpression();
		}
		if(type == TokenType.STRING) {
      return getString();
		}
    if(type == TokenType.INDEX_EXPRESSION) {
      return getIndexExpression();
    }
    return getOperator();     
  }

  private String getNumber() throws SyntaxError {
    StringBuilder sb = new StringBuilder();
    char c = expression.charAt(currentPosition);
    if(c == '-') {
      sb.append(c);
      currentPosition++;
    }
    while (true) {
      c = expression.charAt(currentPosition);
      if(Character.isDigit(c) || c == '.' || c == 'e' || c == 'E') {
        sb.append(c);
      } else {
        if(isOperator(c) || Character.isWhitespace(c) || c == ']') {
          break;
        } else {
          throw new SyntaxError("Invalid expression: " + expression);
        }
      }
      currentPosition++;
      if(currentPosition == expression.length()) {
        break;
      }
    }
    return sb.toString();
  }

	private String getExpression() throws SyntaxError {
		int braCount = 1;
    currentPosition++;  // skip ( character
    StringBuilder sb = new StringBuilder();

    while(currentPosition < expression.length()) {
      char c = expression.charAt(currentPosition);
			if(c == '(') {
				braCount++;
			}

      if(c == ')') {
				braCount--;
				if(braCount == 0) {
					break;
				}
      } 
			
			sb.append(c);
			currentPosition++;
      if(currentPosition == expression.length()) {
        throw new SyntaxError("Unmatched parentheses");
      }
		}
    return sb.toString();
  }

  private String getVariableOrFunction() throws SyntaxError {
    StringBuilder sb = new StringBuilder();
    char c = expression.charAt(currentPosition);
    if(c == '-') {
      sb.append(c);
      currentPosition++;
    }
    while(true) {
      c = expression.charAt(currentPosition);
      if(c == '[') {
        currentType = TokenType.ARRAY;
        break;
      }
			if(c == '(') {
				currentType = TokenType.FUNCTION;
				break;
			}
      if(isOperator(c) || Character.isWhitespace(c)) {
        break;                        
      } else {
        if(c == '$' || Character.isLetterOrDigit(c) || c == '_') {
          sb.append(c);
        } else {
          throw new SyntaxError("Invalid expression: found " + c + " in " + expression);
        }
      }
      currentPosition++;
      if(currentPosition == expression.length()) {
        break;
      }
    }    
    skipWhitespace();
    if(currentPosition < expression.length()) {
      lookAhead();
      if(nextType == TokenType.INDEX_EXPRESSION) {
        currentType = TokenType.ARRAY;
      } 
      else {
        if(nextType == TokenType.EXPRESSION && previousType == TokenType.UNARY_OPERATOR) {
          System.out.println("next type = " + nextType + ", setting current type to function?");
          System.out.println("previous type = " + previousType);
          System.out.println("current type = " + currentType);
          currentType = TokenType.FUNCTION;
        } 
      }
    }       
    return sb.toString();     
  }

	private String getString() {
		currentPosition++;
		StringBuilder b = new StringBuilder();
		char c;
		while(currentPosition < expression.length()) {
			c = expression.charAt(currentPosition);
			currentPosition++;
			if(c == '"') {
				break;
			}
			b.append(c);
		}
		return b.toString();
	}
  
	private String getIndexExpression() throws SyntaxError {
		currentPosition++;
    System.out.println("getIndexExpression: " + expression.substring(currentPosition));
    int close = expression.lastIndexOf(']');
    if (close < 0) {
      close = expression.length();
    }
    String substring = expression.substring(currentPosition, close);
    currentPosition = close+1;
    System.out.println("Expression tokenizer array index expression = " + substring);
    return substring;
	}

  private String getOperator() throws SyntaxError {
    StringBuilder b = new StringBuilder();
		char c;
    int count = 0;
		while(isOperator(c = expression.charAt(currentPosition)) && count < 2) {
			b.append(c);
			currentPosition++;
      count++;
			if(c == '-' || c == '+') {
				break;
			}
		}
		String ret = b.toString();
		if("".equals(ret)) {
			throw new SyntaxError("Unknown operator: " + c);
		}
    return b.toString();
  }

  private void skipWhitespace() throws SyntaxError {
    try {
      char c = expression.charAt(currentPosition);
      if (c == ',') {
        throw new SyntaxError(", not allowed in expressions");
      }
      while( (Character.isWhitespace(c) || c == ')') && currentPosition < expression.length()) {
        currentPosition++;
        c = expression.charAt(currentPosition);
      }
    } catch (StringIndexOutOfBoundsException se) {
    }
  }

  private boolean isOperator(char c) {
    return OPERATORS.indexOf(c) >= 0; 
  }

  private static void printTokenType(TokenType tt) {
    System.out.println(tt.toString());
  }

  public static void main(String[] args) {
    try {
      //String exp = "mod(5, a[1,1])";
      //String exp = "x-4";
      String exp = "substring(a$, 6, 6)";
      //String exp = "int(mod(4, 3))";
      //String exp = "a[0, a[0, 1]]";
      //String exp = "len (\"wdsflksdfkl\")";
			//String exp = "1++ - 3 * ab(funcargs) + (54 - b)";
			//String exp = "1+3 + 10*(1+2) + 5 * 3";
			//String exp = "2++9, (12+4), sin(3*2+9, 53)";
			//String exp = "\"string\"+ 1";
      //String exp = "-3**2";
			//String exp = "-1";
      //String exp = "1+1, 3+(5*4), 5-(3/2)";
      //String exp = "a, 20";
      //String exp = "10+ 20";
      
			System.out.println("expression: " + exp);
      ExpressionTokenizer et = new ExpressionTokenizer(exp);
      while(et.hasMoreTokens()) {
        String token = et.getNextToken();
        System.out.println("Token: " + token);
        TokenType tt = et.getTokenType();
        printTokenType(tt);        
      }

    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
