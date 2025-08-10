package org.apache.hc.core5.http.nio;

import java.io.IOException;

public interface AsyncDataProducer extends ResourceHolder {
  int available();
  
  void produce(DataStreamChannel paramDataStreamChannel) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/AsyncDataProducer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */