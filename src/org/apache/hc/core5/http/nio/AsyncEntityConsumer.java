package org.apache.hc.core5.http.nio;

import java.io.IOException;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;

public interface AsyncEntityConsumer<T> extends AsyncDataConsumer {
  void streamStart(EntityDetails paramEntityDetails, FutureCallback<T> paramFutureCallback) throws HttpException, IOException;
  
  void failed(Exception paramException);
  
  T getContent();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/AsyncEntityConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */