package io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;

public interface FlowablePublishClassic<T> {
  Publisher<T> publishSource();
  
  int publishBufferSize();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowablePublishClassic.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */