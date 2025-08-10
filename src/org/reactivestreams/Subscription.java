package org.reactivestreams;

public interface Subscription {
  void request(long paramLong);
  
  void cancel();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/reactivestreams/Subscription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */