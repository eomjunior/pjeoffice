package org.apache.log4j.spi;

import org.apache.log4j.Appender;
import org.apache.log4j.Category;

public interface HierarchyEventListener {
  void addAppenderEvent(Category paramCategory, Appender paramAppender);
  
  void removeAppenderEvent(Category paramCategory, Appender paramAppender);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/spi/HierarchyEventListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */