package com.github.taskresolver4j;

public interface IExecutorContext extends IFailureAlerter {
  boolean isClosing();
  
  boolean isBatchState();
  
  boolean isRunningInBatch();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/taskresolver4j/IExecutorContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */