package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;

@FunctionalInterface
@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface AsyncFunction<I, O> {
  ListenableFuture<O> apply(@ParametricNullness I paramI) throws Exception;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/AsyncFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */