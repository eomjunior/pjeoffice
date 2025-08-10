package org.apache.log4j.spi;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;

public interface ErrorHandler extends OptionHandler {
  void setLogger(Logger paramLogger);
  
  void error(String paramString, Exception paramException, int paramInt);
  
  void error(String paramString);
  
  void error(String paramString, Exception paramException, int paramInt, LoggingEvent paramLoggingEvent);
  
  void setAppender(Appender paramAppender);
  
  void setBackupAppender(Appender paramAppender);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/spi/ErrorHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */