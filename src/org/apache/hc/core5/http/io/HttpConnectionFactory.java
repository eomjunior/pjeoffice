package org.apache.hc.core5.http.io;

import java.io.IOException;
import java.net.Socket;

public interface HttpConnectionFactory<T extends org.apache.hc.core5.http.HttpConnection> {
  T createConnection(Socket paramSocket) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/HttpConnectionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */