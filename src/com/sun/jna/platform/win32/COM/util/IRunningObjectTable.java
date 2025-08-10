package com.sun.jna.platform.win32.COM.util;

import java.util.List;

public interface IRunningObjectTable {
  Iterable<IDispatch> enumRunning();
  
  <T> List<T> getActiveObjectsByInterface(Class<T> paramClass);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/sun/jna/platform/win32/COM/util/IRunningObjectTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */