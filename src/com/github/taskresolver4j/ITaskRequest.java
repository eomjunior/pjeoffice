package com.github.taskresolver4j;

import com.github.progress4j.IProgressFactory;

public interface ITaskRequest<O> {
  boolean isValid(StringBuilder paramStringBuilder);
  
  ITask<O> getTask(IProgressFactory paramIProgressFactory, IExecutorContext paramIExecutorContext);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/taskresolver4j/ITaskRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */