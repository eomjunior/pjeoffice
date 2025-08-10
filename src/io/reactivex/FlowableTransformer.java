package io.reactivex;

import io.reactivex.annotations.NonNull;
import org.reactivestreams.Publisher;

public interface FlowableTransformer<Upstream, Downstream> {
  @NonNull
  Publisher<Downstream> apply(@NonNull Flowable<Upstream> paramFlowable);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/FlowableTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */