package org.bouncycastle.pkcs;

import java.io.IOException;

public class PKCSIOException extends IOException {
  private Throwable cause;
  
  public PKCSIOException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public PKCSIOException(String paramString) {
    super(paramString);
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pkcs/PKCSIOException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */