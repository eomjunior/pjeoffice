package org.bouncycastle.cms;

import java.io.IOException;
import java.io.OutputStream;

public interface CMSProcessable {
  void write(OutputStream paramOutputStream) throws IOException, CMSException;
  
  Object getContent();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cms/CMSProcessable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */