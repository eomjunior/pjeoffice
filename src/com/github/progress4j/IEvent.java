package com.github.progress4j;

public interface IEvent extends IState {
  String getMessage();
  
  int getStackSize();
  
  boolean isIndeterminated();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/IEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */