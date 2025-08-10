package com.github.taskresolver4j;

import com.github.utils4j.imp.function.IProvider;

public interface ITask<T> extends IProvider<ITaskResponse<T>> {
  String getId();
  
  boolean isValid(StringBuilder paramStringBuilder);
  
  default void dispose() {}
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/taskresolver4j/ITask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */