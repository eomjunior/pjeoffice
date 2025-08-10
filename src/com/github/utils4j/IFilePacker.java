package com.github.utils4j;

import java.io.File;
import java.util.List;

public interface IFilePacker<T extends Exception> extends ILifeCycle<T> {
  void reset();
  
  List<File> filesPackage() throws InterruptedException;
  
  void offer(List<File> paramList) throws InterruptedException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/IFilePacker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */