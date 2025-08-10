package org.apache.hc.core5.http.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.CharacterCodingException;
import org.apache.hc.core5.util.CharArrayBuffer;

public interface SessionOutputBuffer {
  boolean hasData();
  
  int capacity();
  
  int length();
  
  int flush(WritableByteChannel paramWritableByteChannel) throws IOException;
  
  void write(ByteBuffer paramByteBuffer);
  
  void write(ReadableByteChannel paramReadableByteChannel) throws IOException;
  
  void writeLine(CharArrayBuffer paramCharArrayBuffer) throws CharacterCodingException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/SessionOutputBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */