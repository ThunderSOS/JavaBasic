/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.happysoft.basic.expression.function;

import org.happysoft.basic.SyntaxError;
import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.TokenType;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class POW extends Function {

  public int getNumArgs() {
    return 2;
  }

  public Argument eval(Argument... args) throws SyntaxError {
    double pow = Math.pow(args[0].getDoubleValue(), args[1].getDoubleValue());
    Argument a = new Argument("" + pow, TokenType.NUMBER);
    return a;
  }
}
