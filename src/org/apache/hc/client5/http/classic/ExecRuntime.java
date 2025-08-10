package org.apache.hc.client5.http.classic;

import java.io.IOException;
import org.apache.hc.client5.http.HttpRoute;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.annotation.Internal;
import org.apache.hc.core5.concurrent.CancellableDependency;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.util.TimeValue;

@Internal
public interface ExecRuntime {
  boolean isExecutionAborted();
  
  boolean isEndpointAcquired();
  
  void acquireEndpoint(String paramString, HttpRoute paramHttpRoute, Object paramObject, HttpClientContext paramHttpClientContext) throws IOException;
  
  void releaseEndpoint();
  
  void discardEndpoint();
  
  boolean isEndpointConnected();
  
  void disconnectEndpoint() throws IOException;
  
  void connectEndpoint(HttpClientContext paramHttpClientContext) throws IOException;
  
  void upgradeTls(HttpClientContext paramHttpClientContext) throws IOException;
  
  ClassicHttpResponse execute(String paramString, ClassicHttpRequest paramClassicHttpRequest, HttpClientContext paramHttpClientContext) throws IOException, HttpException;
  
  boolean isConnectionReusable();
  
  void markConnectionReusable(Object paramObject, TimeValue paramTimeValue);
  
  void markConnectionNonReusable();
  
  ExecRuntime fork(CancellableDependency paramCancellableDependency);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/classic/ExecRuntime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */