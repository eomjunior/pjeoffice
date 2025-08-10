package org.apache.hc.core5.http.io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.hc.core5.annotation.Internal;
import org.apache.hc.core5.http.ClassicHttpRequest;

@Internal
public interface ResponseOutOfOrderStrategy {
  boolean isEarlyResponseDetected(ClassicHttpRequest paramClassicHttpRequest, HttpClientConnection paramHttpClientConnection, InputStream paramInputStream, long paramLong1, long paramLong2) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/ResponseOutOfOrderStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */