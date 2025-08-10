package org.bouncycastle.pkcs;

public class PKCSException extends Exception {
  private Throwable cause;
  
  public PKCSException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public PKCSException(String paramString) {
    super(paramString);
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pkcs/PKCSException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */