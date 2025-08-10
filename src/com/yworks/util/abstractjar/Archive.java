package com.yworks.util.abstractjar;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.Manifest;

public interface Archive {
  String getName();
  
  Enumeration<Entry> getEntries();
  
  Manifest getManifest() throws IOException;
  
  InputStream getInputStream(Entry paramEntry) throws IOException;
  
  void close() throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/util/abstractjar/Archive.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */