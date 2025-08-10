package com.yworks.common;

import java.io.File;

public interface ShrinkBag {
  void setIn(File paramFile);
  
  void setOut(File paramFile);
  
  File getIn();
  
  File getOut();
  
  boolean isEntryPointJar();
  
  void setResources(String paramString);
  
  ResourcePolicy getResources();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/common/ShrinkBag.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */