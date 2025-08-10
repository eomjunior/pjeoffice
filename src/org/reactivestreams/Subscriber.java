package org.reactivestreams;

public interface Subscriber<T> {
  void onSubscribe(Subscription paramSubscription);
  
  void onNext(T paramT);
  
  void onError(Throwable paramThrowable);
  
  void onComplete();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/reactivestreams/Subscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */