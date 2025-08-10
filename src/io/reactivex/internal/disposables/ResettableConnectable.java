package io.reactivex.internal.disposables;

import io.reactivex.annotations.Experimental;
import io.reactivex.disposables.Disposable;

@Experimental
public interface ResettableConnectable {
  void resetIf(Disposable paramDisposable);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/disposables/ResettableConnectable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */