package org.happysoft.basic.expression.function;

import java.util.HashMap;
import org.happysoft.basic.SyntaxError;

/**
 * @author Chris Francis (c_francis1@yahoo.com)
 */
public class FunctionFactory {

  private static FunctionFactory instance;
  private final static HashMap<String, Function> functions = new HashMap<String, Function>();

  static {
    instance = new FunctionFactory();
    functions.put("LN", new LN());
    functions.put("LOG", new LOG());
    functions.put("SIN", new SIN());
    functions.put("COS", new COS());
    functions.put("TAN", new TAN());
    functions.put("SQR", new SQR());
    functions.put("MOD", new MOD());
    functions.put("RND", new RND());
    functions.put("ROUND", new ROUND());
    functions.put("INT", new INT());
    functions.put("LEN", new LEN());
    functions.put("TIME", new TIME());
    functions.put("LOCAL_TIME", new LOCAL_TIME());
    functions.put("S_HEIGHT", new S_HEIGHT());
    functions.put("S_WIDTH", new S_WIDTH());
    functions.put("SUBSTRING", new SUBSTRING());
    functions.put("CODE", new CODE());
    functions.put("CHR$", new CHR$());
    functions.put("STR$", new STR$());
    functions.put("VAL", new VAL());
    functions.put("POW", new POW());
  }

  private FunctionFactory() {
  }

  public static FunctionFactory getInstance() {
    return instance;
  }

  public Function getFunction(String function) throws SyntaxError {
    Function fn = functions.get(function.toUpperCase());
    if (fn == null) {
      throw new SyntaxError("Function not found: " + function);
    }
    return fn;
  }

  public void addUDF(String name, Function udf) {
    functions.put(name.toUpperCase(), udf);
  }

}
