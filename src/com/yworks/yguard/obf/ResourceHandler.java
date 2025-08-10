package com.yworks.yguard.obf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ResourceHandler {
  boolean filterName(String paramString, StringBuffer paramStringBuffer);
  
  boolean filterContent(InputStream paramInputStream, OutputStream paramOutputStream, String paramString) throws IOException;
  
  String filterString(String paramString1, String paramString2) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/ResourceHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */