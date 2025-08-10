package com.fasterxml.jackson.core.util;

public interface JacksonFeature {
  boolean enabledByDefault();
  
  int getMask();
  
  boolean enabledIn(int paramInt);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/core/util/JacksonFeature.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */