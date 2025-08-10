package io.reactivex;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public interface MaybeObserver<T> {
  void onSubscribe(@NonNull Disposable paramDisposable);
  
  void onSuccess(@NonNull T paramT);
  
  void onError(@NonNull Throwable paramThrowable);
  
  void onComplete();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/MaybeObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */