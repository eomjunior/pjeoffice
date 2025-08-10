package com.sun.jna.platform.win32.COM;

public interface IPersistStream extends IPersist {
  boolean IsDirty();
  
  void Load(IStream paramIStream);
  
  void Save(IStream paramIStream);
  
  void GetSizeMax();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/sun/jna/platform/win32/COM/IPersistStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */