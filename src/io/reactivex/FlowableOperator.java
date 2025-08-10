package io.reactivex;

import io.reactivex.annotations.NonNull;
import org.reactivestreams.Subscriber;

public interface FlowableOperator<Downstream, Upstream> {
  @NonNull
  Subscriber<? super Upstream> apply(@NonNull Subscriber<? super Downstream> paramSubscriber) throws Exception;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/FlowableOperator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */