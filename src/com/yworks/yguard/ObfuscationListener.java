package com.yworks.yguard;

import java.util.EventListener;

public interface ObfuscationListener extends EventListener {
  void obfuscatingJar(String paramString1, String paramString2);
  
  void obfuscatingClass(String paramString);
  
  void parsingClass(String paramString);
  
  void parsingJar(String paramString);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/ObfuscationListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */