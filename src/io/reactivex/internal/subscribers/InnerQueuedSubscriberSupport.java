package io.reactivex.internal.subscribers;

public interface InnerQueuedSubscriberSupport<T> {
  void innerNext(InnerQueuedSubscriber<T> paramInnerQueuedSubscriber, T paramT);
  
  void innerError(InnerQueuedSubscriber<T> paramInnerQueuedSubscriber, Throwable paramThrowable);
  
  void innerComplete(InnerQueuedSubscriber<T> paramInnerQueuedSubscriber);
  
  void drain();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscribers/InnerQueuedSubscriberSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */