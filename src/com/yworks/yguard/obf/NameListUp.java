package com.yworks.yguard.obf;

public interface NameListUp {
  String getMethodOutNameUp(String paramString1, String paramString2) throws ClassNotFoundException;
  
  String getMethodObfNameUp(String paramString1, String paramString2) throws ClassNotFoundException;
  
  String getFieldOutNameUp(String paramString) throws ClassNotFoundException;
  
  String getFieldObfNameUp(String paramString) throws ClassNotFoundException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/NameListUp.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */