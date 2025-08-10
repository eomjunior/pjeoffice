package org.apache.hc.client5.http.classic;

import java.io.IOException;
import org.apache.hc.core5.annotation.Contract;
import org.apache.hc.core5.annotation.ThreadingBehavior;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpException;

@Contract(threading = ThreadingBehavior.STATELESS)
public interface ExecChainHandler {
  ClassicHttpResponse execute(ClassicHttpRequest paramClassicHttpRequest, ExecChain.Scope paramScope, ExecChain paramExecChain) throws IOException, HttpException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/classic/ExecChainHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */