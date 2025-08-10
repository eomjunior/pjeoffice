package io.reactivex.internal.util;

import io.reactivex.Observer;

public interface ObservableQueueDrain<T, U> {
  boolean cancelled();
  
  boolean done();
  
  Throwable error();
  
  boolean enter();
  
  int leave(int paramInt);
  
  void accept(Observer<? super U> paramObserver, T paramT);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/ObservableQueueDrain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */