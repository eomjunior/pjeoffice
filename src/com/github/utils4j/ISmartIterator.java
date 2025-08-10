package com.github.utils4j;

import java.util.Iterator;

public interface ISmartIterator<T> extends Iterator<T> {
  void reset();
  
  boolean hasPrevious();
  
  T previous();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/ISmartIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */