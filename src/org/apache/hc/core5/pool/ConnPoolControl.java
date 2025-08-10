package org.apache.hc.core5.pool;

import java.util.Set;
import org.apache.hc.core5.util.TimeValue;

public interface ConnPoolControl<T> extends ConnPoolStats<T> {
  void setMaxTotal(int paramInt);
  
  int getMaxTotal();
  
  void setDefaultMaxPerRoute(int paramInt);
  
  int getDefaultMaxPerRoute();
  
  void setMaxPerRoute(T paramT, int paramInt);
  
  int getMaxPerRoute(T paramT);
  
  void closeIdle(TimeValue paramTimeValue);
  
  void closeExpired();
  
  Set<T> getRoutes();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/pool/ConnPoolControl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */