package com.itextpdf.text.log;

public interface Logger {
  Logger getLogger(Class<?> paramClass);
  
  Logger getLogger(String paramString);
  
  boolean isLogging(Level paramLevel);
  
  void warn(String paramString);
  
  void trace(String paramString);
  
  void debug(String paramString);
  
  void info(String paramString);
  
  void error(String paramString);
  
  void error(String paramString, Exception paramException);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/log/Logger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */