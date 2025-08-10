package org.bouncycastle.openssl;

public class EncryptionException extends PEMException {
  private Throwable cause;
  
  public EncryptionException(String paramString) {
    super(paramString);
  }
  
  public EncryptionException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/openssl/EncryptionException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */