package org.apache.hc.core5.http2;

import java.util.List;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpException;

public interface H2MessageConverter<T extends org.apache.hc.core5.http.HttpMessage> {
  T convert(List<Header> paramList) throws HttpException;
  
  List<Header> convert(T paramT) throws HttpException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/H2MessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */