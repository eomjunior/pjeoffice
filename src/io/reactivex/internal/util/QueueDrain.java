package io.reactivex.internal.util;

import org.reactivestreams.Subscriber;

public interface QueueDrain<T, U> {
  boolean cancelled();
  
  boolean done();
  
  Throwable error();
  
  boolean enter();
  
  long requested();
  
  long produced(long paramLong);
  
  int leave(int paramInt);
  
  boolean accept(Subscriber<? super U> paramSubscriber, T paramT);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/QueueDrain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */