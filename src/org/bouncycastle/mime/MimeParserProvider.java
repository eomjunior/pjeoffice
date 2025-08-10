package org.bouncycastle.mime;

import java.io.IOException;
import java.io.InputStream;

public interface MimeParserProvider {
  MimeParser createParser(InputStream paramInputStream) throws IOException;
  
  MimeParser createParser(Headers paramHeaders, InputStream paramInputStream) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mime/MimeParserProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */