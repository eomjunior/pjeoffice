package org.bouncycastle.operator;

import java.io.OutputStream;

public interface AADProcessor {
  OutputStream getAADStream();
  
  byte[] getMAC();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/operator/AADProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */