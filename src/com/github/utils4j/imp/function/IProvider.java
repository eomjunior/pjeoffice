package com.github.utils4j.imp.function;

@FunctionalInterface
public interface IProvider<T> {
  T get() throws Exception;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/function/IProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */