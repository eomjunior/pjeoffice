package org.apache.hc.core5.http.nio;

public interface NHttpMessageWriterFactory<T extends org.apache.hc.core5.http.MessageHeaders> {
  NHttpMessageWriter<T> create();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/NHttpMessageWriterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */