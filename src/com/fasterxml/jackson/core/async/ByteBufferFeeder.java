package com.fasterxml.jackson.core.async;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface ByteBufferFeeder extends NonBlockingInputFeeder {
  void feedInput(ByteBuffer paramByteBuffer) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/core/async/ByteBufferFeeder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */