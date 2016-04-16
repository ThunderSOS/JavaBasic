
package org.happysoft.basic.expression;

import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.operator.UnaryOperatorFactory;
import org.happysoft.basic.expression.operator.OperatorFactory;
import org.happysoft.basic.expression.operator.Operator;
import org.happysoft.basic.expression.operator.OperatorPrecedence;
import org.happysoft.basic.function.FunctionFactory;
import org.happysoft.basic.function.Function;
import java.util.Stack;
import org.happysoft.basic.var.VariableTable;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class Expression {

	private Stack<Operator> operatorStack = new Stack<Operator>();
	private Stack<Argument> argumentStack = new Stack<Argument>();

	private int opStackSize = 0;
	private String expression;

	private int previousOperatorPrecedence = 0;
	private int highestPrecedenceOperator = 0;

	public Expression(String expression) throws SyntaxError {
		this.expression = expression;
	}

	public double eval() throws SyntaxError {
		ExpressionTokenizer t = new ExpressionTokenizer(expression);

		while(t.hasMoreTokens()) {
			String token = t.getNextToken();
			TokenType tt = t.getTokenType();

			System.out.println("Token: " + token + " [" + tt + "]");
			// highest priority unary first
			if(tt == TokenType.UNARY_OPERATOR) {
				System.out.println("unary: " + token);
				Operator op = UnaryOperatorFactory.getInstance().getOperator(token);
				// we need to get the next argument and apply the operator immediately.
				String nextToken = t.getNextToken();
				TokenType nextTt = t.getTokenType();
				System.out.println("next type = " + nextTt);
				if(nextTt == TokenType.OPERATOR) {
					throw new SyntaxError("Argument expected");
				}
				if(nextTt == TokenType.EXPRESSION) {
					doExpression(nextToken);
					// the next argument is on the stack
					Argument arg = argumentStack.peek();
					double newValue = op.eval(arg.getValue());
					arg.setValue("" + newValue);

				}	else {
					if(nextTt == TokenType.FUNCTION) {
						String functionArgs = t.getNextToken();
						doFunction(nextToken, functionArgs);
						// the next argument is on the stack
						Argument arg = argumentStack.peek();
						double newValue = op.eval(arg.getValue());
						arg.setValue("" + newValue);
					} else {
						Argument arg = ArgumentFactory.getInstance().getArgument(nextToken, nextTt);
						double newValue = op.eval(arg.getValue());
						arg.setValue("" + newValue);
						argumentStack.push(arg);
					}
				}
				checkAndEval();
				continue;
			}

			if(tt == TokenType.EXPRESSION) {
				doExpression(token);
				checkAndEval();
				continue;
			}

			if(tt == TokenType.FUNCTION) {
				String arguments = t.getNextToken();
				doFunction(token, arguments);
				checkAndEval();
				continue;
			}

			if(tt == TokenType.OPERATOR) {
				previousOperatorPrecedence = highestPrecedenceOperator;
				Operator op = OperatorFactory.getInstance().getOperator(token.charAt(0));
				int precedence = op.getPrecedence();
				System.out.println("Precedence = " + precedence);
				if(precedence > highestPrecedenceOperator) {
					highestPrecedenceOperator = precedence;
				}
				if(previousOperatorPrecedence > highestPrecedenceOperator) {
					System.out.println("swap top two operators on stack");
				}
				operatorStack.push(op);
				opStackSize++;

			} else {
				Argument arg = ArgumentFactory.getInstance().getArgument(token, tt);
				argumentStack.push(arg);
				checkAndEval();
			}
		}
		evalLast();
		return argumentStack.pop().getValue();
	}

	private void doFunction(String function, String argToken) throws SyntaxError {
		Function fn = FunctionFactory.getInstance().getFunction(function);
		int numArgs = fn.getNumArgs();

		double[] args = getFunctionArgs(argToken, numArgs);
		double newValue = fn.eval(args);
		Argument a = new NumericArgument("" + newValue, TokenType.NUMBER);
		argumentStack.push(a);
	}

	private double[] getFunctionArgs(String token, int numArgs) throws SyntaxError {
		String[] args = token.split(",");
		if(args.length != numArgs && !args[0].equals(")")) {
			throw new SyntaxError(numArgs + " argument(s) expected, got " + args.length);
		}
		double[] d = new double[numArgs];
		for(int i = 0; i < numArgs; i++) {
			doExpression(args[i]);
			Argument a = argumentStack.pop();
			d[i] = a.getValue();
		}
		return d;
	}

	private void doExpression(String token) throws SyntaxError {
		System.out.println("doExpression, token = " + token);
		Expression sub = new Expression(token);
		double subResult = sub.eval();
		Argument a = new NumericArgument("" + subResult, TokenType.NUMBER);
		argumentStack.push(a);
	}

	private void evalLast() {
		while(operatorStack.size() > 0) {
			evalTop();
		}
	}

	private void checkAndEval() {
		System.out.println("check and eval");
		if(opStackSize == 1) {
			Operator o = operatorStack.peek();
			if(o.getPrecedence() == OperatorPrecedence.HIGHEST) {
				evalTop();
			}
		}
		if(opStackSize > 1) {
			evalOneMaybe();
		} 
	}

	private void evalOneMaybe() {
		System.out.println("evalOneMaybe");
		Operator lastButOne = operatorStack.elementAt(opStackSize-2);
		Operator last = operatorStack.peek();
		if (last.getPrecedence() > lastButOne.getPrecedence()) {
			System.out.println("operator precedence: " + last + " over " + lastButOne );
			evalTop();
		} 
	}

	private void evalTop() {
		System.out.println("eval top");
		Operator o = operatorStack.pop();
		Argument arg2 = argumentStack.pop();
		Argument arg1 = argumentStack.pop();
		System.out.println("evaluate: " + arg1.value + " " + o + " " + arg2.value);
		double d = o.eval(arg1.getValue(), arg2.getValue());
		argumentStack.push(new NumericArgument("" + d, TokenType.NUMBER));
		opStackSize--;
		debugStacks();
	}

	public void debugStacks() {
		for(Argument a : argumentStack) {
			System.out.println(a);
		}
		for(Operator o : operatorStack) {
			System.out.println(o);
		}
	}

	public static void main(String[] args) {
		//String e = "(5|2)+1";
		//String e = "rnd()";
		//String e = "1/(1+sin( (2+-1)+0.5707963267948966))";
		//String e = "1+3 + 10*(2+(-1)) * sin( (2+-1)+0.5707963267948966) * 1";
		VariableTable.getInstance().setVariable("z", 15d);
		VariableTable.getInstance().setVariable("x", 20d);
		String e = "2^3&2*4";
		System.out.println("res=" + (2^3&2*4));
		System.out.println("res=" + (2^3&8));
		System.out.println("res=" + (2^0));
		//String e = "((4--mod(z, 4)) + 3)*x
		//String e = "10, 20, 30";
		//String e = "-1+2*3";
		//String e = "(2+-1) + 5";
		//String e = "2+-1";
		//String e = "1+((2+5)*2)";
		//String e = "1+(2+(-1))";
		//String e = "sin((2-1)+0.5707963267948966)";
		System.out.println(Math.PI/2);
		System.out.println(Math.sin(Math.PI/2));
		try {
			Expression p = new Expression(e);
			double result = p.eval();
			System.out.println("result = " + result);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
