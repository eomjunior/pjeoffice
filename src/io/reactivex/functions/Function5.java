package io.reactivex.functions;

import io.reactivex.annotations.NonNull;

public interface Function5<T1, T2, T3, T4, T5, R> {
  @NonNull
  R apply(@NonNull T1 paramT1, @NonNull T2 paramT2, @NonNull T3 paramT3, @NonNull T4 paramT4, @NonNull T5 paramT5) throws Exception;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/functions/Function5.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */