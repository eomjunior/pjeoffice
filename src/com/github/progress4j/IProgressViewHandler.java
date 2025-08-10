package com.github.progress4j;

import io.reactivex.Observable;

public interface IProgressViewHandler {
  void showSteps(boolean paramBoolean);
  
  Observable<Boolean> detailStatus();
  
  Observable<Boolean> cancelClick();
  
  boolean isCanceled();
  
  boolean isStepsVisible();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/IProgressViewHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */