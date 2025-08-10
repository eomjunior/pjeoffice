package com.sun.jna.platform.win32.COM.util;

import com.sun.jna.platform.win32.OaIdl;

public interface IDispatch extends IUnknown {
  <T> void setProperty(String paramString, T paramT);
  
  <T> T getProperty(Class<T> paramClass, String paramString, Object... paramVarArgs);
  
  <T> T invokeMethod(Class<T> paramClass, String paramString, Object... paramVarArgs);
  
  <T> void setProperty(OaIdl.DISPID paramDISPID, T paramT);
  
  <T> T getProperty(Class<T> paramClass, OaIdl.DISPID paramDISPID, Object... paramVarArgs);
  
  <T> T invokeMethod(Class<T> paramClass, OaIdl.DISPID paramDISPID, Object... paramVarArgs);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/sun/jna/platform/win32/COM/util/IDispatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */