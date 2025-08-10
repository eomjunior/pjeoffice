package com.github.utils4j.imp.function;

import java.util.Optional;

@FunctionalInterface
public interface IOptionalTransformer<T> {
  Optional<T> transform(T paramT);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/function/IOptionalTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */