package com.github.utils4j.imp.function;

@FunctionalInterface
public interface IProcedure<R, E extends Exception> {
  R call() throws E;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/function/IProcedure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */