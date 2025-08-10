package com.sun.jna.platform.win32.COM.util;

import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.util.annotation.ComInterface;

@ComInterface(iid = "{00000000-0000-0000-C000-000000000046}")
public interface IUnknown {
  <T> T queryInterface(Class<T> paramClass) throws COMException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/sun/jna/platform/win32/COM/util/IUnknown.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */