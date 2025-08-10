package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.util.function.Supplier;

@FunctionalInterface
@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface Supplier<T> extends Supplier<T> {
  @ParametricNullness
  T get();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/Supplier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */