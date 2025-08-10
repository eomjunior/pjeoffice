package io.reactivex;

import io.reactivex.annotations.NonNull;

public interface MaybeOperator<Downstream, Upstream> {
  @NonNull
  MaybeObserver<? super Upstream> apply(@NonNull MaybeObserver<? super Downstream> paramMaybeObserver) throws Exception;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/MaybeOperator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */