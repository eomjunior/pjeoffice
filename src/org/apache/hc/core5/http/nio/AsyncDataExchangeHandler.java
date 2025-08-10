package org.apache.hc.core5.http.nio;

public interface AsyncDataExchangeHandler extends AsyncDataConsumer, AsyncDataProducer {
  void failed(Exception paramException);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/AsyncDataExchangeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */