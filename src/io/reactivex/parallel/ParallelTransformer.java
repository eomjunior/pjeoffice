package io.reactivex.parallel;

import io.reactivex.annotations.NonNull;

public interface ParallelTransformer<Upstream, Downstream> {
  @NonNull
  ParallelFlowable<Downstream> apply(@NonNull ParallelFlowable<Upstream> paramParallelFlowable);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/parallel/ParallelTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */