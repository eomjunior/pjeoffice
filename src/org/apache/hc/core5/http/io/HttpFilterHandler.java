package org.apache.hc.core5.http.io;

import java.io.IOException;
import org.apache.hc.core5.annotation.Contract;
import org.apache.hc.core5.annotation.ThreadingBehavior;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.protocol.HttpContext;

@Contract(threading = ThreadingBehavior.STATELESS)
public interface HttpFilterHandler {
  void handle(ClassicHttpRequest paramClassicHttpRequest, HttpFilterChain.ResponseTrigger paramResponseTrigger, HttpContext paramHttpContext, HttpFilterChain paramHttpFilterChain) throws HttpException, IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/HttpFilterHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */