package org.apache.hc.client5.http.classic.methods;

import org.apache.hc.client5.http.config.Configurable;
import org.apache.hc.core5.http.ClassicHttpRequest;

public interface HttpUriRequest extends ClassicHttpRequest, Configurable {
  void abort() throws UnsupportedOperationException;
  
  boolean isAborted();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/classic/methods/HttpUriRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */