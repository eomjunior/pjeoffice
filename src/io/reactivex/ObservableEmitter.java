package io.reactivex;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;

public interface ObservableEmitter<T> extends Emitter<T> {
  void setDisposable(@Nullable Disposable paramDisposable);
  
  void setCancellable(@Nullable Cancellable paramCancellable);
  
  boolean isDisposed();
  
  @NonNull
  ObservableEmitter<T> serialize();
  
  boolean tryOnError(@NonNull Throwable paramThrowable);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/ObservableEmitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */