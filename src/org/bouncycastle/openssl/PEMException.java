package org.bouncycastle.openssl;

import java.io.IOException;

public class PEMException extends IOException {
  Exception underlying;
  
  public PEMException(String paramString) {
    super(paramString);
  }
  
  public PEMException(String paramString, Exception paramException) {
    super(paramString);
    this.underlying = paramException;
  }
  
  public Exception getUnderlyingException() {
    return this.underlying;
  }
  
  public Throwable getCause() {
    return this.underlying;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/openssl/PEMException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */