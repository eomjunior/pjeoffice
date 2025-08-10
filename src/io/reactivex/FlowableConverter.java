package io.reactivex;

import io.reactivex.annotations.NonNull;

public interface FlowableConverter<T, R> {
  @NonNull
  R apply(@NonNull Flowable<T> paramFlowable);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/FlowableConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */