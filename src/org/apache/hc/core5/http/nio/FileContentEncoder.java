package org.apache.hc.core5.http.nio;

import java.io.IOException;
import java.nio.channels.FileChannel;

public interface FileContentEncoder extends ContentEncoder {
  long transfer(FileChannel paramFileChannel, long paramLong1, long paramLong2) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/FileContentEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */