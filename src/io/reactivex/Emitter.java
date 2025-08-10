package io.reactivex;

import io.reactivex.annotations.NonNull;

public interface Emitter<T> {
  void onNext(@NonNull T paramT);
  
  void onError(@NonNull Throwable paramThrowable);
  
  void onComplete();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/Emitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */