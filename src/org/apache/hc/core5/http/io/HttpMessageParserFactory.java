package org.apache.hc.core5.http.io;

import org.apache.hc.core5.http.config.Http1Config;

public interface HttpMessageParserFactory<T extends org.apache.hc.core5.http.MessageHeaders> {
  HttpMessageParser<T> create(Http1Config paramHttp1Config);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/HttpMessageParserFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */