package org.apache.hc.core5.http.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import org.apache.hc.core5.http.Header;

public interface ContentDecoder {
  int read(ByteBuffer paramByteBuffer) throws IOException;
  
  boolean isCompleted();
  
  List<? extends Header> getTrailers();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/ContentDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */