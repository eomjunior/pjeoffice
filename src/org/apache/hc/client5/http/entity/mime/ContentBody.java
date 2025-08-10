package org.apache.hc.client5.http.entity.mime;

import java.io.IOException;
import java.io.OutputStream;

public interface ContentBody extends ContentDescriptor {
  String getFilename();
  
  void writeTo(OutputStream paramOutputStream) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/mime/ContentBody.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */