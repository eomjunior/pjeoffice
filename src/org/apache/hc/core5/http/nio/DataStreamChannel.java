package org.apache.hc.core5.http.nio;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.List;
import org.apache.hc.core5.annotation.Contract;
import org.apache.hc.core5.annotation.ThreadingBehavior;
import org.apache.hc.core5.http.Header;

@Contract(threading = ThreadingBehavior.SAFE)
public interface DataStreamChannel extends StreamChannel<ByteBuffer> {
  void requestOutput();
  
  int write(ByteBuffer paramByteBuffer) throws IOException;
  
  void endStream(List<? extends Header> paramList) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/DataStreamChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */