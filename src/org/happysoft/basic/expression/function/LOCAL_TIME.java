package org.happysoft.basic.expression.function;

import java.util.Calendar;
import org.happysoft.basic.expression.Argument;
import org.happysoft.basic.expression.TokenType;

/**
 * @author Chris
 */
public class LOCAL_TIME extends Function {

  @Override
  public int getNumArgs() {
    return 0;
  }

  @Override
  public Argument eval(Argument... args) {
    long time = Calendar.getInstance().getTimeInMillis();
    Argument a = new Argument("" + time, TokenType.NUMBER);
    return a;
  }
}
