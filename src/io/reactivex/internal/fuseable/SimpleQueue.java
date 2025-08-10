package io.reactivex.internal.fuseable;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public interface SimpleQueue<T> {
  boolean offer(@NonNull T paramT);
  
  boolean offer(@NonNull T paramT1, @NonNull T paramT2);
  
  @Nullable
  T poll() throws Exception;
  
  boolean isEmpty();
  
  void clear();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/fuseable/SimpleQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */