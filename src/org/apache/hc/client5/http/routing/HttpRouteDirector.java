package org.apache.hc.client5.http.routing;

import org.apache.hc.client5.http.RouteInfo;
import org.apache.hc.core5.annotation.Contract;
import org.apache.hc.core5.annotation.ThreadingBehavior;

@Contract(threading = ThreadingBehavior.STATELESS)
public interface HttpRouteDirector {
  public static final int UNREACHABLE = -1;
  
  public static final int COMPLETE = 0;
  
  public static final int CONNECT_TARGET = 1;
  
  public static final int CONNECT_PROXY = 2;
  
  public static final int TUNNEL_TARGET = 3;
  
  public static final int TUNNEL_PROXY = 4;
  
  public static final int LAYER_PROTOCOL = 5;
  
  int nextStep(RouteInfo paramRouteInfo1, RouteInfo paramRouteInfo2);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/routing/HttpRouteDirector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */