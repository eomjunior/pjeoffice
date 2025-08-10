package com.github.signer4j;

public interface IWindowLockDettector {
  void start();
  
  void stop();
  
  IWindowLockDettector notifyTo(IWorkstationLockListener paramIWorkstationLockListener);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/IWindowLockDettector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */