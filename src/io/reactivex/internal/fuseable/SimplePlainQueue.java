package io.reactivex.internal.fuseable;

import io.reactivex.annotations.Nullable;

public interface SimplePlainQueue<T> extends SimpleQueue<T> {
  @Nullable
  T poll();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/fuseable/SimplePlainQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */