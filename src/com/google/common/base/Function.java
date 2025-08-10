package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.util.function.Function;
import javax.annotation.CheckForNull;

@FunctionalInterface
@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface Function<F, T> extends Function<F, T> {
  @ParametricNullness
  T apply(@ParametricNullness F paramF);
  
  boolean equals(@CheckForNull Object paramObject);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/Function.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */