package io.reactivex.internal.observers;

public interface InnerQueuedObserverSupport<T> {
  void innerNext(InnerQueuedObserver<T> paramInnerQueuedObserver, T paramT);
  
  void innerError(InnerQueuedObserver<T> paramInnerQueuedObserver, Throwable paramThrowable);
  
  void innerComplete(InnerQueuedObserver<T> paramInnerQueuedObserver);
  
  void drain();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/InnerQueuedObserverSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */