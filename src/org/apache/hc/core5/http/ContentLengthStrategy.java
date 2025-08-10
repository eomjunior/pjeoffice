package org.apache.hc.core5.http;

import org.apache.hc.core5.annotation.Contract;
import org.apache.hc.core5.annotation.ThreadingBehavior;

@Contract(threading = ThreadingBehavior.STATELESS)
public interface ContentLengthStrategy {
  public static final long CHUNKED = -1L;
  
  public static final long UNDEFINED = -9223372036854775807L;
  
  long determineLength(HttpMessage paramHttpMessage) throws HttpException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/ContentLengthStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */