package org.apache.hc.core5.function;

@FunctionalInterface
public interface Decorator<T> {
  T decorate(T paramT);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/function/Decorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */