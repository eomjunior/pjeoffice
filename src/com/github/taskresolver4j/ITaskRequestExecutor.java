package com.github.taskresolver4j;

import com.github.taskresolver4j.exception.TaskExecutorException;

public interface ITaskRequestExecutor<I, O> {
  void notifyClosing();
  
  void notifyOpening();
  
  void async(Runnable paramRunnable);
  
  void close() throws InterruptedException;
  
  void execute(I paramI, O paramO) throws TaskExecutorException;
  
  long getRunningTasks();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/taskresolver4j/ITaskRequestExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */