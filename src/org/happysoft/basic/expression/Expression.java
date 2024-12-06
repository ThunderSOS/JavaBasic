package org.happysoft.basic.expression;

import java.util.ArrayList;
import java.util.Stack;

import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.operator.UnaryOperatorFactory;
import org.happysoft.basic.expression.operator.OperatorFactory;
import org.happysoft.basic.expression.operator.Operator;
import org.happysoft.basic.expression.function.FunctionFactory;
import org.happysoft.basic.expression.function.Function;
import org.happysoft.basic.expression.ExpressionResult.ResultType;
import org.happysoft.basic.var.NumericArrayTable;
import org.happysoft.basic.var.StringTable;
import org.happysoft.basic.var.VariableTable;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class Expression {

  private Stack<Operator> operatorStack = new Stack<Operator>();
  private Stack<Argument> argumentStack = new Stack<Argument>();
  private LocalArgumentMap localArgs = null;

  private String expression;

  private ResultType resultType = ResultType.NUMERIC;

  public Expression(String expression) throws SyntaxError {
    expression = expression.trim();
    this.expression = expression;
  }

  public Expression(String expression, LocalArgumentMap localArgs) throws SyntaxError {
    expression = expression.trim();
    this.expression = expression;
    this.localArgs = localArgs;
  }

  public ExpressionResult eval() throws SyntaxError {
    ExpressionTokenizer t = new ExpressionTokenizer(expression);

    while (t.hasMoreTokens()) {
      String token = t.getNextToken();
      TokenType tt = t.getTokenType();
      System.out.println("Token: " + token + "[" + tt + "]");

      if (tt == TokenType.UNARY_OPERATOR) {
        Operator op = UnaryOperatorFactory.getInstance().getOperator(token);
        // we need to get the next argument and apply the operator immediately.
        String nextToken = t.getNextToken();
        TokenType nextTt = t.getTokenType();

        if (nextTt == TokenType.OPERATOR) {
          throw new SyntaxError("Argument expected");
        } else {
          if (nextTt == TokenType.NUMBER) {
            Argument a = new Argument(nextToken, TokenType.NUMBER);
            ExpressionResult result = op.eval(a);
            argumentStack.push(result.getArgument());
            continue;
          }
          if (nextTt == TokenType.VARIABLE) {
            Argument a = ArgumentFactory.getInstance().getArgument(nextToken, TokenType.VARIABLE, null, null);
            ExpressionResult result = op.eval(a);
            argumentStack.push(result.getArgument());
            continue;
          }
        }

        if (nextTt == TokenType.ARRAY) {
          String indexExpressionToken = t.getNextToken();
          TokenType type = t.getTokenType();

          if (type != TokenType.INDEX_EXPRESSION) {
            throw new SyntaxError("Index expected");
          }

          doArray(nextToken, indexExpressionToken);
          Argument arg = argumentStack.peek();
          // now apply the unary operator
          ExpressionResult newValue = op.eval(arg);
          arg.setValue(newValue.getArgument().getStringValue());
          continue;
        }

        if (nextTt == TokenType.EXPRESSION) {
          doExpression(nextToken);
          // the next argument is now on the stack
          Argument arg = argumentStack.peek();
          ExpressionResult newValue = op.eval(arg);
          arg.setValue(newValue.getArgument().getStringValue());

        } else {
          if (nextTt == TokenType.FUNCTION) {
            String functionArgs = t.getNextToken();
            doFunction(nextToken, functionArgs);
            // the next argument is now on the stack
            Argument arg = argumentStack.peek();
            ExpressionResult newValue = op.eval(arg);
            arg.setValue(newValue.getArgument().getStringValue());
          }
        }
        continue;
      }

      if (tt == TokenType.ARRAY) {
        String nextToken = t.getNextToken();
        TokenType nextTt = t.getTokenType();

        if (nextTt != TokenType.INDEX_EXPRESSION) {
          throw new SyntaxError("Index expected");
        }
        doArray(token, nextToken);
        continue;
      }

      if (tt == TokenType.EXPRESSION) {
        doExpression(token);
        continue;
      }

      if (tt == TokenType.FUNCTION) {
        String arguments = t.getNextToken();
        doFunction(token, arguments);
        continue;
      }

      if (tt == TokenType.OPERATOR) {
        Operator op = OperatorFactory.getInstance().getOperator(token);
        operatorStack.push(op);

      } else {
        // variable
        Argument arg = ArgumentFactory.getInstance().getArgument(token, tt, null, localArgs);
        argumentStack.push(arg);
      }
    }

    evalHighest();
    ExpressionResult result = new ExpressionResult(argumentStack.pop());
    switch (resultType) {
      case BOOLEAN:
        result.setResultType(ExpressionResult.ResultType.BOOLEAN);
        break;

      case STRING:
        result.setResultType(ExpressionResult.ResultType.STRING);
        break;
    }
    return result;
  }

  private void doFunction(String function, String argToken) throws SyntaxError {
    Function fn = FunctionFactory.getInstance().getFunction(function);
    System.out.println("Applying function: " + fn.getClass().getSimpleName());
    int numArgs = fn.getNumArgs();
    System.out.println("Num args: " + numArgs + ": " + argToken);
    Argument[] args = getFunctionArgs(argToken, numArgs);
    Argument a = fn.eval(args);
    argumentStack.push(a);
  }

  private Argument[] getFunctionArgs(String token, int numArgs) throws SyntaxError {
    String[] args = split(token); // split evaluates the sub-expressions!    
    if (args.length != numArgs && !(args[0].equals(""))) {
      throw new SyntaxError(numArgs + " argument(s) expected, got " + args.length);
    }
    Argument[] arr = new Argument[numArgs];
    for (int i = 0; i < numArgs; i++) {
      Argument a = argumentStack.pop();
      arr[args.length - i - 1] = a;
    }
    return arr;
  }

  private void doArray(String arrayName, String indexExpressionToken) throws SyntaxError {
    System.out.println("Doing array index: " + indexExpressionToken);
    int numDimensions = doIndexExpression(indexExpressionToken);
    // the index arguments are now on the stack
    int[] dimensions = new int[numDimensions];

    for (int i = 0; i < numDimensions; i++) {
      Argument indexArgument = argumentStack.pop();
      if (!(indexArgument.getTokenType() == TokenType.NUMBER || indexArgument.getTokenType() == TokenType.VARIABLE)) {
        throw new SyntaxError("Numeric expression expected");
      }
      if (!(indexArgument.getTokenType() == TokenType.NUMBER)) {
        throw new SyntaxError("Numeric expression expected");
      }
      dimensions[numDimensions - i - 1] = (int) indexArgument.getIntValue();
    }
    Argument arg = ArgumentFactory.getInstance().getArgument(arrayName, TokenType.ARRAY, dimensions, localArgs);
    argumentStack.push(arg);
  }

  private String[] split(String token) throws SyntaxError {
    String[] strings = StringUtils.split(token, ',');
    for (String s : strings) {
      Expression sub = new Expression(s.trim(), localArgs);
      ExpressionResult subResult = sub.eval();
      if (subResult.resultType == ExpressionResult.ResultType.BOOLEAN) {
        resultType = ResultType.BOOLEAN;
      }
      Argument a = subResult.getArgument();
      argumentStack.push(a);
    }
    return strings;
  }

  private int doIndexExpression(String token) throws SyntaxError {
    // this sequence is the same as in Statement, refactor. 
    ArrayList<String> strings = new ArrayList<String>();
    int braCount = 0;
    char[] chars = token.toCharArray();
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];

      if (c == '[' || c == '(') {
        braCount++;
      }
      if (c == ']' || c == ')') {
        braCount--;
      }
      if (c == ',' && braCount == 0) {
        strings.add(sb.toString());
        sb = new StringBuilder();
        continue;
      }
      sb.append(c);
      if (i == chars.length - 1 && c != ',') {
        strings.add(sb.toString());
        break;
      }
    }

    for (String s : strings) {
      Expression sub = new Expression(s.trim(), localArgs);
      ExpressionResult subResult = sub.eval();
      if (subResult.resultType == ExpressionResult.ResultType.BOOLEAN) {
        resultType = ResultType.BOOLEAN;
      }
      Argument a = subResult.getArgument();
      argumentStack.push(a);
    }

    return strings.size();
  }

  /**
   * Evaluate a subexpression and place the result on the argument stack
   *
   * @param token the expression
   * @throws SyntaxError
   */
  private void doExpression(String token) throws SyntaxError {
    Expression sub = new Expression(token, localArgs);
    ExpressionResult subResult = sub.eval();
    if (subResult.resultType == ExpressionResult.ResultType.BOOLEAN) {
      resultType = ResultType.BOOLEAN;
    }
    Argument a = subResult.getArgument();
    argumentStack.push(a);
    //debugStacks();
  }

  private int getHighestPrecedenceOperatorIndex() {
    int highestIndex = 0;
    int highestPrecedence = 0;
    for (int i = 0; i < operatorStack.size(); i++) {
      Operator op = operatorStack.elementAt(i);
      int precedence = op.getPrecedence();
      if (precedence > highestPrecedence) {
        highestPrecedence = precedence;
        highestIndex = i;
      }
    }
    return highestIndex;
  }

  private void evalHighest() throws SyntaxError {
    while (operatorStack.size() > 0) {
      int highestPriorityOperatorIndex = getHighestPrecedenceOperatorIndex();
      evalN(highestPriorityOperatorIndex);
    }
  }

  /**
   * Evaluate the n'th and n+1'th arguments on the argument stack by applying
   * the corresponding n'th operator. The result is placed back in the stack in
   * the n'th position.
   *
   * @param n
   * @throws SyntaxError if two arguments cannot be found.
   */
  private void evalN(int n) throws SyntaxError {
    //System.out.println("Evaluate " + n);
    //debugStacks();
    Argument arg1 = argumentStack.remove(n);
    Operator o = operatorStack.remove(n);
    try {
      Argument arg2 = argumentStack.remove(n);

      if (resultType == ResultType.BOOLEAN && !o.isBoolean()
              && !(arg1.getTokenType() == TokenType.STRING || arg2.getTokenType() == TokenType.STRING)) {
        throw new SyntaxError("Cannot apply " + o + " to boolean expression: " + expression);
      }

      if (o.isBoolean()) {
        resultType = ResultType.BOOLEAN;
      }

      ExpressionResult result = evalOperation(o, arg1, arg2);
      argumentStack.insertElementAt(result.getArgument(), n);

    } catch (ArrayIndexOutOfBoundsException ae) {
      throw new SyntaxError("No argument for " + o.toString());
    }
  }

//  private void debugStacks() {
//    int i = 0;
//    if(argumentStack.isEmpty()) {
//      System.out.println("Argument stack is empty");
//    } else {
//      System.out.println("Stack contains:");    
//      for(Argument a: argumentStack) {
//        System.out.println("argument " + i++ + " = " + a.value);
//      }
//    }
//  }
  private ExpressionResult evalOperation(Operator o, Argument arg1, Argument arg2) throws SyntaxError {
    ExpressionResult result = o.eval(arg1, arg2);
    return result;
  }

  public String getExpression() {
    return expression;
  }

  public static void main(String[] args) {
    try {
      VariableTable.getInstance().setVariable("z", 15d);
      VariableTable.getInstance().setVariable("x", 20d);
      StringTable.getInstance().setStringVariable("f$", "jeff");
      VariableTable.getInstance().setVariable("n", 41d);
      NumericArrayTable.getInstance().createArray("a", 10, 10);
      NumericArrayTable.getInstance().setValue("a", 2, 0, 0);

      //String e = "substring(f$, 1, 3)";
      //String e = "-PI";
      //String e = "-x-3";
      //String e = "-sin (0.5707963267948966 + 1)";
      //String e = "mod(len(\"str,ing\"), a[0,0])";  
      //String e = "mod(5, a[0,0])";
      //String e = "mod(65, 60) + 20";
      //String e = "10 + 20 + 50";
      //String e = "a[0, a[0, 1]]";
      //String e = "2**2**3";
      //String e = "-3**2";
      //String e = "-(-3)";
      //String e = "len (\"SDFKJSDHF\")";
      //String e = "f$ + 2";
      //String e = "1*2<=2&&-3==-3";
      //String e = "(5 | 2) +  1";
      //String e = "-(1+2)";
      //String e = "round(100*rnd())";
      //String e = "1/(1+sin( (2+-1)+0.5707963267948966))";
      //String e = "1+3 + 10*(2+(-1)) * sin( (2+-1)+0.5707963267948966) * 1";
      //String e = "2^3&2*4";
      //String e = "(((4--mod(z, 4)) + 3)*x <= 200) == True";
      //String e = "(((4--mod(z, 4)) + 3)*x <= 200)";
      //String e = "4--mod(z[5], 4)";
      //String e = "x[5,4]*100";
      //String e = "10, 20, 30";
      //String e = "";
      //String e = "1++2";
      //String e = "-1+2*3";
      //String e = "(2+-1) + 5";
      //String e = "-(1--1)";
      //String e = "1--1";
      //String e = "1+(2+5*2)";
      //String e = "1+(2+-1)";
      String e = "sin((2-1)+0.5707963267948966)";
      //String e = "2*3+1 +";
      //String e = " int (3.5)";
      //String e = "\"jeff\" + 1 * 3 + \"keith\"";
      //String e = "1 + \":\" + 2";
      //String e = "1+2 THEN";
      //String e = "\"40\"";

      Expression p = new Expression(e);
      ExpressionResult result = p.eval();
      System.out.println("result = " + result.getArgument().getStringValue() + " [" + result.getResultType() + "]");

    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }

}
