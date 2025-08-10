package org.apache.hc.client5.http.classic;

import org.apache.hc.client5.http.HttpRoute;

public interface BackoffManager {
  void backOff(HttpRoute paramHttpRoute);
  
  void probe(HttpRoute paramHttpRoute);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/classic/BackoffManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */