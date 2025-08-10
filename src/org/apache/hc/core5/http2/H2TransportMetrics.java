package org.apache.hc.core5.http2;

import org.apache.hc.core5.http.io.HttpTransportMetrics;

public interface H2TransportMetrics extends HttpTransportMetrics {
  long getFramesTransferred();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/H2TransportMetrics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */