package com.yworks.yshrink.core;

public interface ClassResolver extends AutoCloseable {
  Class resolve(String paramString) throws ClassNotFoundException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/core/ClassResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */