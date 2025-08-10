package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface FutureCallback<V> {
  void onSuccess(@ParametricNullness V paramV);
  
  void onFailure(Throwable paramThrowable);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/FutureCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */