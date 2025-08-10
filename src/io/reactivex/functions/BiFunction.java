package io.reactivex.functions;

import io.reactivex.annotations.NonNull;

public interface BiFunction<T1, T2, R> {
  @NonNull
  R apply(@NonNull T1 paramT1, @NonNull T2 paramT2) throws Exception;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/functions/BiFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */