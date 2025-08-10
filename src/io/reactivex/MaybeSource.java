package io.reactivex;

import io.reactivex.annotations.NonNull;

public interface MaybeSource<T> {
  void subscribe(@NonNull MaybeObserver<? super T> paramMaybeObserver);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/MaybeSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */