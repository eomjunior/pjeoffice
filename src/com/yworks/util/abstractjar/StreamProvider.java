package com.yworks.util.abstractjar;

import java.io.DataInputStream;
import java.io.IOException;

public interface StreamProvider {
  DataInputStream getNextClassEntryStream() throws IOException;
  
  DataInputStream getNextResourceEntryStream() throws IOException;
  
  Entry getCurrentEntry();
  
  String getCurrentEntryName();
  
  String getCurrentDir();
  
  String getCurrentFilename();
  
  void reset();
  
  void close() throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/util/abstractjar/StreamProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */