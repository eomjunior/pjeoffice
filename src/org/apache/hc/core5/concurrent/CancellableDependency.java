package org.apache.hc.core5.concurrent;

public interface CancellableDependency extends Cancellable {
  void setDependency(Cancellable paramCancellable);
  
  boolean isCancelled();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/concurrent/CancellableDependency.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */