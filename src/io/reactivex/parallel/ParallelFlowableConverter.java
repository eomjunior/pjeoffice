package io.reactivex.parallel;

import io.reactivex.annotations.NonNull;

public interface ParallelFlowableConverter<T, R> {
  @NonNull
  R apply(@NonNull ParallelFlowable<T> paramParallelFlowable);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/parallel/ParallelFlowableConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */