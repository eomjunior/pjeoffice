package com.github.utils4j;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface IParam {
  String getName();
  
  boolean isPresent();
  
  <T> T get();
  
  <T> T orElse(T paramT);
  
  <T> T orElseGet(Supplier<? extends T> paramSupplier);
  
  <T> void ifPresent(Consumer<T> paramConsumer);
  
  <X extends Throwable, T> T orElseThrow(Supplier<? extends X> paramSupplier) throws X;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/IParam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */