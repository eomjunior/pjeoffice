package org.bouncycastle.cert;

import java.io.IOException;

public class CertIOException extends IOException {
  private Throwable cause;
  
  public CertIOException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public CertIOException(String paramString) {
    super(paramString);
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cert/CertIOException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */