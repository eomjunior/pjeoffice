package org.apache.hc.core5.http.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import org.apache.hc.core5.util.CharArrayBuffer;

public interface SessionInputBuffer {
  boolean hasData();
  
  int length();
  
  int fill(ReadableByteChannel paramReadableByteChannel) throws IOException;
  
  int read();
  
  int read(ByteBuffer paramByteBuffer, int paramInt);
  
  int read(ByteBuffer paramByteBuffer);
  
  int read(WritableByteChannel paramWritableByteChannel, int paramInt) throws IOException;
  
  int read(WritableByteChannel paramWritableByteChannel) throws IOException;
  
  boolean readLine(CharArrayBuffer paramCharArrayBuffer, boolean paramBoolean) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/SessionInputBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */