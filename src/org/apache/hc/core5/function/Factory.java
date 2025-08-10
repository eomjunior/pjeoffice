package org.apache.hc.core5.function;

@FunctionalInterface
public interface Factory<P, T> {
  T create(P paramP);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/function/Factory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */