package com.github.utils4j.imp.function;

@FunctionalInterface
public interface ICreator<T, R, E extends Exception> {
  R create(T paramT) throws E;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/function/ICreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */