package io.reactivex.internal.fuseable;

import java.util.concurrent.Callable;

public interface ScalarCallable<T> extends Callable<T> {
  T call();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/fuseable/ScalarCallable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */