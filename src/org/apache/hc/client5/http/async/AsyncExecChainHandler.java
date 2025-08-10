package org.apache.hc.client5.http.async;

import java.io.IOException;
import org.apache.hc.core5.annotation.Contract;
import org.apache.hc.core5.annotation.ThreadingBehavior;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.nio.AsyncEntityProducer;

@Contract(threading = ThreadingBehavior.STATELESS)
public interface AsyncExecChainHandler {
  void execute(HttpRequest paramHttpRequest, AsyncEntityProducer paramAsyncEntityProducer, AsyncExecChain.Scope paramScope, AsyncExecChain paramAsyncExecChain, AsyncExecCallback paramAsyncExecCallback) throws HttpException, IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/async/AsyncExecChainHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */