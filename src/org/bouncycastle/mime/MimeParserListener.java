package org.bouncycastle.mime;

import java.io.IOException;
import java.io.InputStream;

public interface MimeParserListener {
  MimeContext createContext(MimeParserContext paramMimeParserContext, Headers paramHeaders);
  
  void object(MimeParserContext paramMimeParserContext, Headers paramHeaders, InputStream paramInputStream) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mime/MimeParserListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */