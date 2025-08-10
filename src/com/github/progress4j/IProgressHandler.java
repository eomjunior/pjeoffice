package com.github.progress4j;

import com.github.utils4j.ICanceller;
import com.github.utils4j.IDisposable;

public interface IProgressHandler<T extends java.awt.Container> extends ICanceller, IIsContainer<T>, IDisposable, IProgressViewHandler {
  void stepToken(IStepEvent paramIStepEvent);
  
  void stageToken(IStageEvent paramIStageEvent);
  
  void cancel();
  
  void bind(Thread paramThread);
  
  boolean isFrom(Thread paramThread);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/IProgressHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */