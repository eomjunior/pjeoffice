package org.bouncycastle.mime;

import java.io.IOException;
import java.io.InputStream;

public class ConstantMimeContext implements MimeContext, MimeMultipartContext {
  public InputStream applyContext(Headers paramHeaders, InputStream paramInputStream) throws IOException {
    return paramInputStream;
  }
  
  public MimeContext createContext(int paramInt) throws IOException {
    return this;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mime/ConstantMimeContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */