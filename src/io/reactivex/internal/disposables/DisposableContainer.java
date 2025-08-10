package io.reactivex.internal.disposables;

import io.reactivex.disposables.Disposable;

public interface DisposableContainer {
  boolean add(Disposable paramDisposable);
  
  boolean remove(Disposable paramDisposable);
  
  boolean delete(Disposable paramDisposable);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/disposables/DisposableContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */