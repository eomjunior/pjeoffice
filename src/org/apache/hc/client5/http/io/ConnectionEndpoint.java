package org.apache.hc.client5.http.io;

import java.io.IOException;
import org.apache.hc.core5.annotation.Contract;
import org.apache.hc.core5.annotation.ThreadingBehavior;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.impl.io.HttpRequestExecutor;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.io.ModalCloseable;
import org.apache.hc.core5.util.Timeout;

@Contract(threading = ThreadingBehavior.SAFE)
public abstract class ConnectionEndpoint implements ModalCloseable {
  public abstract ClassicHttpResponse execute(String paramString, ClassicHttpRequest paramClassicHttpRequest, HttpRequestExecutor paramHttpRequestExecutor, HttpContext paramHttpContext) throws IOException, HttpException;
  
  public abstract boolean isConnected();
  
  public abstract void setSocketTimeout(Timeout paramTimeout);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/io/ConnectionEndpoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */