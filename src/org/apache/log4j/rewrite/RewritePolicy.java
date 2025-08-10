package org.apache.log4j.rewrite;

import org.apache.log4j.spi.LoggingEvent;

public interface RewritePolicy {
  LoggingEvent rewrite(LoggingEvent paramLoggingEvent);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/rewrite/RewritePolicy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */