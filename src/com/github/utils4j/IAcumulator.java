package com.github.utils4j;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface IAcumulator<T> extends Consumer<T>, Supplier<T> {
  T handleFail(IOException paramIOException);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/IAcumulator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */